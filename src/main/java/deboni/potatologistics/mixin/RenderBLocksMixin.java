package deboni.potatologistics.mixin;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.BlockAutoBasket;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turniplabs.halplibe.helper.TextureHelper;
import turniplabs.halplibe.util.TextureHandler;


@Mixin(
        value={RenderBlocks.class},
        remap=false
)

public abstract class RenderBLocksMixin {
    @Shadow
    private WorldSource blockAccess;

    @Shadow private World world;

    @Shadow protected abstract boolean renderBlockBasket(Block block, int x, int y, int z);

    @Inject(
            method = "renderBlockByRenderType",
            at = @At("HEAD"),
            cancellable = true
    )

    void renderBlockByRenderType(Block block, int renderType, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (PotatoLogisticsMod.blockPipe != null && block.id == PotatoLogisticsMod.blockPipe.id || PotatoLogisticsMod.blockDirectionalPipe != null && block.id == PotatoLogisticsMod.blockDirectionalPipe.id) {
            cir.setReturnValue(render((RenderBlocks) ((Object)this), this.blockAccess, x, y, z, block, world));
        } else if (PotatoLogisticsMod.blockAutoBasket != null && PotatoLogisticsMod.blockAutoBasket.id == block.id) {
            cir.setReturnValue(renderBlockAutoBasket(((RenderBlocks)(Object)this), block, x, y, z));
        } else if (PotatoLogisticsMod.blockTreeChoper != null && PotatoLogisticsMod.blockTreeChoper.id == block.id) {
            cir.setReturnValue(renderTreeChopper((RenderBlocks) ((Object)this), this.blockAccess, x, y, z, block, world));
        }
    }

    @Unique
    private boolean renderBlockAutoBasket(RenderBlocks renderblocks, Block block, int x, int y, int z) {
        float onepix = 0.0625f;
        float basketHeight = 1.0f;
        block.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f);
        renderblocks.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0625f);
        renderblocks.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.9375f, 1.0f, 1.0f, 1.0f);
        renderblocks.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.0625f, 0.0625f, 1.0f, 0.9375f);
        renderblocks.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.9375f, 0.0f, 0.0625f, 1.0f, 1.0f, 0.9375f);
        renderblocks.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        int height = ((BlockAutoBasket)block).getFillLevel(this.world, x, y, z);
        if (height > 0) {
            renderblocks.renderTopFace(block, x, (float)(y - 1) + 0.0625f + 0.0625f * (float)height, z, Block.texCoordToIndex(5, 8));
        }
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        return true;
    }
    @Unique
    private static boolean renderTreeChopper(RenderBlocks renderblocks, WorldSource blockAccess, int x, int y, int z, Block block, World world) {
        int meta = blockAccess.getBlockMetadata(x, y, z);

        Direction direction = Direction.getDirectionById(meta & 7);
        float pixelSize = 1.0f / 16.0f;
        float halfPixelSize = pixelSize * 0.5f;

        float offset = pixelSize * 6;
        float size = 1.0f;
        float min = 0.5f - size * 0.5f;
        float max = 0.5f + size * 0.5f;

        if (direction == Direction.NORTH) {
            block.setBlockBounds(0.0f, 0.0f, pixelSize, 1.0f, 1.0f, 1.0f);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    pixelSize * 2, 0.5f, min - offset,
                    1.0f - pixelSize * 2, 0.5f, max - offset
            );
        } else if (direction == Direction.SOUTH) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f - pixelSize);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    pixelSize * 2, 0.5f, min + offset,
                    1.0f - pixelSize * 2, 0.5f, max + offset
            );
        } else if (direction == Direction.EAST) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f - pixelSize, 1.0f, 1.0f);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    min + offset, 0.5f, pixelSize * 2,
                    max + offset, 0.5f, 1.0f - pixelSize * 2
            );
        } else {
            block.setBlockBounds(pixelSize, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    min - offset, 0.5f, pixelSize * 2,
                    max - offset, 0.5f, 1.0f - pixelSize * 2
            );
        }

        renderblocks.renderStandardBlock(PotatoLogisticsMod.blockTreeChopperSaw, x, y, z);
        renderblocks.renderStandardBlock(block, x, y, z);

        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

        return true;
    }

    @Unique
    private static boolean render(RenderBlocks renderblocks, WorldSource blockAccess, int x, int y, int z, Block block, World world) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        int type = meta & 0x03;
        boolean isDirectional = (meta & (1 << 2)) != 0;
        Direction pipeDirection = Direction.getDirectionById(meta >> 3).getOpposite();

        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        if (type == 1) {
            g = 0.5f;
            b = 0.0f;
        } else if (type == 2) {
            r = 0.28f;
            g = 0.5f;
        }

        block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);

        int[][] offsets = {
                { 0, -1,  0},
                { 0,  1,  0},
                { 0,  0, -1},
                { 0,  0,  1},
                {-1,  0,  0},
                { 1,  0,  0},
        };

        float[][] coords = {
                {0.26f, 0.00f, 0.26f, 0.74f, 0.25f, 0.74f},
                {0.26f, 0.74f, 0.26f, 0.74f, 1.00f, 0.74f},

                {0.26f, 0.26f, 0.00f, 0.74f, 0.74f, 0.25f},
                {0.26f, 0.26f, 0.74f, 0.74f, 0.74f, 1.00f},

                {0.00f, 0.26f, 0.26f, 0.25f, 0.74f, 0.74f},
                {0.74f, 0.26f, 0.26f, 1.00f, 0.74f, 0.74f},
        };

        float[][] coordsOpen = {
                {0.30f, 0.00f, 0.30f, 0.70f, 0.25f, 0.70f},
                {0.30f, 0.70f, 0.30f, 0.70f, 1.00f, 0.70f},

                {0.30f, 0.30f, 0.00f, 0.70f, 0.70f, 0.25f},
                {0.30f, 0.30f, 0.70f, 0.70f, 0.70f, 1.00f},

                {0.00f, 0.30f, 0.30f, 0.25f, 0.70f, 0.70f},
                {0.70f, 0.30f, 0.30f, 1.00f, 0.70f, 0.70f},
        };

        for (int i = 0; i < offsets.length; i++) {
            float[] coord = coords[i];

            if (isDirectional && pipeDirection.getId() == i) {
                coord = coordsOpen[i];
            }

            int blockBreakerDirId = 0;
            Block blockN = blockAccess.getBlock(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            int blockNMeta = blockAccess.getBlockMetadata(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            if (blockN instanceof BlockRotatable) {
                blockBreakerDirId = BlockRotatable.getOrientation(blockNMeta);
            }

            int nid = blockAccess.getBlockId(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            TileEntity te = blockAccess.getBlockTileEntity(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            if(te instanceof TileEntityPipe
                    || type != 0 && te instanceof IInventory
                    || Direction.getDirectionById(i) == Direction.UP && nid == PotatoLogisticsMod.blockAutoBasket.id
                    || nid == PotatoLogisticsMod.blockBlockCrusher.id && blockBreakerDirId == i
            ) {
                block.setBlockBounds(coord[0], coord[1], coord[2], coord[3], coord[4], coord[5]);
                if (te instanceof TileEntityPipe) {
                    TileEntityPipe pipe = (TileEntityPipe) te;
                    if (isDirectional && pipe.isDirectional()) {
                        if (pipeDirection.getId() == i || pipe.isPointingTo(x, y, z)) {
                            renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
                        }
                    } else {
                        renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
                    }
                } else {
                    renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);
                }
            }
        }

        block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        return true;
    }
}

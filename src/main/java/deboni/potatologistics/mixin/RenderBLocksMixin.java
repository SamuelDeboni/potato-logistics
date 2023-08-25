package deboni.potatologistics.mixin;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(
        value={RenderBlocks.class},
        remap=false
)

public abstract class RenderBLocksMixin {
    @Shadow
    private WorldSource blockAccess;

    @Shadow private World world;

    @Inject(
            method = "renderBlockByRenderType",
            at = @At("HEAD"),
            cancellable = true
    )

    void renderBlockByRenderType(Block block, int renderType, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (PotatoLogisticsMod.pipe != null && block.id == PotatoLogisticsMod.pipe.id || PotatoLogisticsMod.directionalPipe != null && block.id == PotatoLogisticsMod.directionalPipe.id) {
            cir.setReturnValue(render((RenderBlocks) ((Object)this), this.blockAccess, x, y, z, block, world));
        }
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

            int nid  = blockAccess.getBlockId(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            TileEntity te = blockAccess.getBlockTileEntity(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            if(te instanceof TileEntityPipe || type != 0 && te instanceof IInventory) {
                block.setBlockBounds(coord[0], coord[1], coord[2], coord[3], coord[4], coord[5]);
                if (te instanceof TileEntityPipe) {
                    renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, 1, 1, 1);
                } else {
                    renderblocks.renderStandardBlockWithColorMultiplier(block, x, y, z, r, g, b);
                }
            }
        }

        TileEntity te = blockAccess.getBlockTileEntity(x, y, z);
        TileEntityPipe pipeEntity = null;
        if (te instanceof TileEntityPipe) {
            pipeEntity = (TileEntityPipe) te;
        }

        if (pipeEntity != null) {
            List<float[]> blockPos = pipeEntity.getStacksInPipePosition();
            List<ItemStack> stacks = pipeEntity.getStacksInPipe();
            for (int i = 0; i < blockPos.size(); i++) {
                float[] pos = blockPos.get(i);
                ItemStack stack = stacks.get(i);
                //stack.getItem();
                Item item = stack.getItem();

                int textureIndex = stack.getIconIndex();
                if (stack.itemID > 16000) {
                    textureIndex = PotatoLogisticsMod.itemContainer.getBlockTextureFromSideAndMetadata(Side.TOP, 0);
                    PotatoLogisticsMod.potatoBlock.setBlockBounds(
                            pos[0] - 0.2f, pos[1] - (1.0f / 32.0f), pos[2]- 0.2f,
                            pos[0] + 0.2f, pos[1] + (1.0f / 32.0f), pos[2] + 0.2f
                    );

                    //EntityItem itemEntity = new EntityItem(world, pos[0] + x, pos[1] + y, pos[2] + z, stack);
                    //EntityRenderer<EntityItem> ier = EntityRenderDispatcher.instance.getRenderer(itemEntity);
                    //ier.doRender(itemEntity, pos[0] + x, pos[1] + y, pos[2] + z, 0, 0);
                } else {
                    PotatoLogisticsMod.potatoBlock.setBlockBounds(
                            pos[0] - 0.2f, pos[1] - 0.2f, pos[2] - 0.2f,
                            pos[0] + 0.2f, pos[1] + 0.2f, pos[2] + 0.2f
                    );
                }
                renderblocks.renderBlockUsingTexture(PotatoLogisticsMod.potatoBlock, 0, x, y, z, textureIndex);
            }
            PotatoLogisticsMod.potatoBlock.setBlockBounds(0, 0, 0, 1, 1, 1);
        }

        block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        return true;
    }
}

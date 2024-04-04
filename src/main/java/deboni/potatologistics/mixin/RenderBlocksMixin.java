package deboni.potatologistics.mixin;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.BlockAutoBasket;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import deboni.potatologistics.blocks.entities.TileEntityStirlingEngine;
import deboni.potatologistics.blocks.entities.TileEntityTreeChopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.HotbarComponent;
import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.RenderBlockSimple;


@Mixin(
        value={RenderBlocks.class},
        remap=false
)

public abstract class RenderBlocksMixin {
    @Shadow
    private WorldSource blockAccess;

    @Shadow private World world;

    @Shadow public abstract void renderBottomFace(Block block, double d, double d1, double d2, int i);

    @Shadow public abstract void renderTopFace(Block block, double d, double d1, double d2, int i);

    @Shadow public abstract void renderNorthFace(Block block, double d, double d1, double d2, int i);

    @Shadow public abstract void renderSouthFace(Block block, double d, double d1, double d2, int i);

    @Shadow public abstract void renderWestFace(Block block, double d, double d1, double d2, int i);

    @Shadow public abstract void renderEastFace(Block block, double d, double d1, double d2, int i);

    @Shadow public boolean useInventoryTint;

    @Shadow public abstract boolean renderStandardBlock(Block block, int x, int y, int z);

    @Unique
    private final RenderBlocks thisAs = (RenderBlocks) ((Object)this);

    @Inject(
            method = "renderBlockByRenderType",
            at = @At("HEAD"),
            cancellable = true
    )
    void renderBlockByRenderType(Block block, int renderType, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (renderType == 150){
            cir.setReturnValue(renderBlockAutoBasket(thisAs, block, x, y, z));
        }
        if (renderType == 151) {
            cir.setReturnValue(renderPipe(thisAs, this.blockAccess, x, y, z, block, world));
        }
        if (renderType == 152) {
            cir.setReturnValue(renderTreeChopper(thisAs, this.blockAccess, x, y, z, block, world));
        }
        if (renderType == 153) {
            cir.setReturnValue(renderEnergyConnector(thisAs, this.blockAccess, x, y, z, block, world));
        }
        if (renderType == 154) {
            cir.setReturnValue(renderBlockStirlingEngine(thisAs, this.blockAccess, x, y, z, block, world));
        }
    }

    @Inject(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IFF)V", at = @At("TAIL"))
    private void renderBlockOnInventory(Block block, int metadata, float brightness, float alpha, CallbackInfo ci){
        int renderType = ((BlockModelRenderBlocks) BlockModelDispatcher.getInstance().getDispatch(block)).renderType;
        if (renderType == 150 || renderType == 151 || renderType == 152){
            renderBlockInvNormal(block, metadata, brightness);
        }
        if (renderType == 154){
            renderBlockStirlingEngineInventory(block, metadata, brightness);
        }
    }

    @Unique
    private void renderBlockInvNormal(Block block, int metadata, float brightness){
        int renderType = ((BlockModelRenderBlocks)BlockModelDispatcher.getInstance().getDispatch(block)).renderType;
        Tessellator tessellator = Tessellator.instance;
        if (this.useInventoryTint) {
            int j = BlockColorDispatcher.getInstance().getDispatch(block).getFallbackColor(metadata);
            float f1 = (float)(j >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(j >> 8 & 0xFF) / 255.0f;
            float f5 = (float)(j & 0xFF) / 255.0f;
            GL11.glColor4f(f1 * brightness, f3 * brightness, f5 * brightness, 1.0f);
        }
        float yOffset = 0.5f;
        if (renderType == 16) {
            metadata = 1;
        }
        if (renderType == 30) {
            yOffset = 0.25f;
        }
        block.setBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5f, 0.0f - yOffset, -0.5f);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, -1.0f);
        this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 0.0f, 1.0f);
        this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0f, 0.0f, 0.0f);
        this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0f, 0.0f, 0.0f);
        this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
    }
    @Unique
    private void renderBlockStirlingEngineInventory(Block block, int metadata, float brightness){
        float onepix = 0.0625f;
        block.setBlockBounds(0, 0.0f, 0, 1, 0.5f - onepix, 1);
        this.renderBlockInvNormal(block,metadata, brightness);

        boolean b = false;
        for (float yf = 0.5f - onepix * 1; yf <= 1.0f; yf += onepix) {
            if (b) {
                block.setBlockBounds(onepix * 2, yf, onepix * 2, 1 - onepix * 2, yf + onepix, 1 - onepix * 2);
            } else {
                block.setBlockBounds(0, yf, 0, 1, yf + onepix, 1);
            }
            renderBlockInvNormal(block,metadata, brightness);

            b = !b;
        }

        block.setBlockBounds(0, 0.0f, 0, 1, 1, 1);
    }


    @Unique
    private static boolean renderBlockStirlingEngine(RenderBlocks renderblocks, WorldSource blockAccess, int x, int y, int z, Block block, World world) {
        float onepix = 0.0625f;
        block.setBlockBounds(0, 0.0f, 0, 1, 0.5f - onepix, 1);
        renderblocks.renderStandardBlock(block, x, y, z);


        float[] heatColor = new float[3];
        heatColor[0] = 1;
        heatColor[1] = 1;
        heatColor[2] = 1;

        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityStirlingEngine) {
            TileEntityStirlingEngine engine = (TileEntityStirlingEngine) te;
            if (engine.temperature > 0) {
                float t = (float)engine.temperature / (float)engine.maxTemperature;
                heatColor[1] = Math.min((1 - t) + t * 0.4f, 1);
                heatColor[2] = Math.min((1 - t) + t * 0.2f, 1);
            }
        }

        boolean b = false;
        for (float yf = 0.5f - onepix * 1; yf < 1.0f; yf += onepix) {
            float t = (yf - 0.4f) * 1.8f;

            if (b) {
                block.setBlockBounds(onepix * 2, yf, onepix * 2, 1 - onepix * 2, yf + onepix, 1 - onepix * 2);
            } else {
                block.setBlockBounds(0, yf, 0, 1, yf + onepix, 1);
            }

            float heatColor_1 = (1 - t) + t * heatColor[1];
            float heatColor_2 = (1 - t) + t * heatColor[2];
            renderblocks.renderStandardBlock(block, x, y, z, heatColor[0], heatColor_1, heatColor_2);

            b = !b;
        }

        block.setBlockBounds(0, 0.0f, 0, 1, 1, 1);
        return true;
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
    private static float[] getConnectorColor(int i ) {
        float r = 0.41f;
        float g = 0.23f;
        float b = 0.18f;
        if (i % 2 == 1) {
            r *= 1.1f;
            g *= 1.1f;
            b *= 1.1f;
        }
        if (i == 0 || i == 8) {
            r = 0.9f;
            g = 0.9f;
            b = 0.9f;
        }

        return new float[]{r, g, b};
    }

    @Unique
    private static boolean renderEnergyConnector(RenderBlocks renderblocks, WorldSource blockAccess, int x, int y, int z, Block block, World world) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        Side side = Side.getSideById(meta & 7);
        float pixelSize = 1.0f / 16.0f;

        if (side == Side.TOP) {
            for (int i = 0; i < 9; i++) {
                float m = (i % 2 == 0) ? pixelSize * 6 : pixelSize * 5;
                block.setBlockBounds(m, pixelSize * i, m, 1 - m, pixelSize * (i + 1), 1 - m);

                float[] color = getConnectorColor(i);
                renderblocks.renderStandardBlock(block, x, y, z, color[0], color[1], color[2]);
            }
        } else if (side == Side.BOTTOM) {
            for (int i = 0; i < 9; i++) {
                float m = (i % 2 == 0) ? pixelSize * 6 : pixelSize * 5;
                block.setBlockBounds(m, 1 - pixelSize * (i + 1), m, 1 - m, 1 - pixelSize * i, 1 - m);

                float[] color = getConnectorColor(i);
                renderblocks.renderStandardBlock(block, x, y, z, color[0], color[1], color[2]);
            }
        } else if (side == Side.NORTH) {
            for (int i = 0; i < 9; i++) {
                float m = (i % 2 == 0) ? pixelSize * 6 : pixelSize * 5;
                block.setBlockBounds(m, m, 1 - pixelSize * (i + 1), 1 - m, 1 - m, 1 - pixelSize * i);

                float[] color = getConnectorColor(i);
                renderblocks.renderStandardBlock(block, x, y, z, color[0], color[1], color[2]);
            }
        } else if (side == Side.SOUTH) {
            for (int i = 0; i < 9; i++) {
                float m = (i % 2 == 0) ? pixelSize * 6 : pixelSize * 5;
                block.setBlockBounds(m, m, pixelSize * i, 1 - m, 1 - m, pixelSize * (i + 1));

                float[] color = getConnectorColor(i);
                renderblocks.renderStandardBlock(block, x, y, z, color[0], color[1], color[2]);
            }
        } else if (side == Side.EAST) {
            for (int i = 0; i < 9; i++) {
                float m = (i % 2 == 0) ? pixelSize * 6 : pixelSize * 5;
                block.setBlockBounds(pixelSize * i, m, m,  pixelSize * (i + 1), 1 - m, 1 - m);

                float[] color = getConnectorColor(i);
                renderblocks.renderStandardBlock(block, x, y, z, color[0], color[1], color[2]);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                float m = (i % 2 == 0) ? pixelSize * 6 : pixelSize * 5;
                block.setBlockBounds(1 - pixelSize * (i + 1), m, m, 1 - pixelSize * i, 1 - m, 1 - m);

                float[] color = getConnectorColor(i);
                renderblocks.renderStandardBlock(block, x, y, z, color[0], color[1], color[2]);
            }
        }

        //block.setBlockBounds(pixelSize * 5, 0, pixelSize * 5, 1 - pixelSize * 5, pixelSize * 9, 1 - pixelSize * 5);
        return true;
    }

    @Unique
    private static boolean renderTreeChopper(RenderBlocks renderblocks, WorldSource blockAccess, int x, int y, int z, Block block, World world) {
        int meta = blockAccess.getBlockMetadata(x, y, z);

        Direction direction = Direction.getDirectionById(meta & 7);
        float pixelSize = 1.0f / 16.0f;
        float halfPixelSize = pixelSize * 0.5f;

        float offset = pixelSize * 3;
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityTreeChopper && ((TileEntityTreeChopper)te).isActive) {
            offset = pixelSize * 7;
        }

        float size = 1.0f;
        float min = 0.5f - size * 0.5f;
        float max = 0.5f + size * 0.5f;

        if (direction == Direction.NORTH) {
            //block.setBlockBounds(0.0f, 0.0f, pixelSize, 1.0f, 1.0f, 1.0f);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    pixelSize * 2, 0.5f, min - offset,
                    1.0f - pixelSize * 2, 0.5f, max - offset
            );
        } else if (direction == Direction.SOUTH) {
            //block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f - pixelSize);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    pixelSize * 2, 0.5f, min + offset,
                    1.0f - pixelSize * 2, 0.5f, max + offset
            );
        } else if (direction == Direction.EAST) {
            //block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f - pixelSize, 1.0f, 1.0f);

            PotatoLogisticsMod.blockTreeChopperSaw.setBlockBounds(
                    min + offset, 0.5f, pixelSize * 2,
                    max + offset, 0.5f, 1.0f - pixelSize * 2
            );
        } else {
            //block.setBlockBounds(pixelSize, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

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
    private static boolean renderPipe(RenderBlocks renderblocks, WorldSource blockAccess, int x, int y, int z, Block block, World world) {
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
        renderblocks.renderStandardBlock(block, x, y, z, r, g, b);

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

            if (isDirectional && pipeDirection.getId() != i) {
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
                            renderblocks.renderStandardBlock(block, x, y, z, 1, 1, 1);
                        }
                    } else {
                        renderblocks.renderStandardBlock(block, x, y, z, 1, 1, 1);
                    }
                } else {
                    renderblocks.renderStandardBlock(block, x, y, z, r, g, b);
                }
            }
        }

        block.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        return true;
    }
}

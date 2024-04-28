package deboni.potatologistics.blocks;

import deboni.potatologistics.Util;
import deboni.potatologistics.blocks.entities.TileEntityTreeChopper;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockTreeChopper extends BlockTileEntityRotatable {

    public BlockTreeChopper(String key, int id, Material material) {
        super(key, id, material);
    }

    private void setTreeChopperDefaultDirection(World world, int x, int y, int z) {
        if (!world.isClientSide) {
            int l = world.getBlockId(x, y, z - 1);
            int i1 = world.getBlockId(x, y, z + 1);
            int j1 = world.getBlockId(x - 1, y, z);
            int k1 = world.getBlockId(x + 1, y, z);
            byte byte0 = 3;

            if (Block.solid[i1] && !Block.solid[l]) {
                byte0 = 2;
            }

            if (Block.solid[j1] && !Block.solid[k1]) {
                byte0 = 5;
            }

            if (Block.solid[k1] && !Block.solid[j1]) {
                byte0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, byte0);
        }
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(Side side, int meta) {
        int index = Sides.orientationLookUp[6 * meta + side.getId()];
        return this.atlasIndices[index];
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId > 0 && Block.blocksList[blockId].canProvidePower()) {
            boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockGettingPowered(x, y, z);
            if (flag) {
                world.scheduleBlockUpdate(x, y, z, this.id, 0);
            }
        }
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        this.setTreeChopperDefaultDirection(world, i, j, k);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityTreeChopper();
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntity te = Util.getBlockTileEntity(world, x, y, z);
        if (te instanceof TileEntityTreeChopper) {
            ((TileEntityTreeChopper) te).dropItems();
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockGettingPowered(x, y, z)) {
            TileEntityTreeChopper te = (TileEntityTreeChopper) Util.getBlockTileEntity(world, x, y, z);
            te.blocksToBreak.clear();
        }
    }
}

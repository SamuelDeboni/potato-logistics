package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.Util;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import deboni.potatologistics.blocks.entities.TileEntiyTreeChopper;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;

import java.util.ArrayList;
import java.util.List;
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
            if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
                byte0 = 3;
            }

            if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
                byte0 = 2;
            }

            if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
                byte0 = 5;
            }

            if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        this.setTreeChopperDefaultDirection(world, i, j, k);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntiyTreeChopper();
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntiyTreeChopper) {
            ((TileEntiyTreeChopper) te).dropItems();
        }
        super.onBlockRemoval(world, x, y, z);
    }


    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockGettingPowered(x, y, z)) {
            TileEntiyTreeChopper te = (TileEntiyTreeChopper) world.getBlockTileEntity(x, y, z);
            //te.breakTree();
            te.blocksToBreak.clear();
        }
    }
}

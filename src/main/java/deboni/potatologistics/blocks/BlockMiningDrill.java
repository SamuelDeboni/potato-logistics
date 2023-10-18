package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntiyTreeChopper;
import deboni.potatologistics.blocks.entities.TileEntityMiningDrill;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockMiningDrill extends BlockTileEntity {
    public BlockMiningDrill(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityMiningDrill();
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
    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityMiningDrill) {
            ((TileEntityMiningDrill) te).dropItems();
        }
        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}

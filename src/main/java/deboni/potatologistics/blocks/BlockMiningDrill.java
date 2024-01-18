package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityMiningDrill;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;

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
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityMiningDrill) {
            ((TileEntityMiningDrill) te).dropItems();
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}

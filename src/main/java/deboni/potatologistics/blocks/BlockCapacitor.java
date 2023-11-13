package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityCapacitor;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

public class BlockCapacitor extends BlockTileEntityRotatable {

    public int capacity;
    public BlockCapacitor(String key, int id, Material material) {
        super(key, id, material);
        capacity = 100000;
    }
    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntityCapacitor capacitor = (TileEntityCapacitor) blockAccess.getBlockTileEntity(x, y, z);
        boolean needPower = !capacitor.needPower();
        PotatoLogisticsMod.LOGGER.info("need power" + needPower + " " + capacitor.energy);
        return needPower;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        return this.isPoweringTo(world, x, y, z, side);
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        world.notifyBlocksOfNeighborChange(i, j - 1, k, this.id);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, this.id);
        world.notifyBlocksOfNeighborChange(i - 1, j, k, this.id);
        world.notifyBlocksOfNeighborChange(i + 1, j, k, this.id);
        world.notifyBlocksOfNeighborChange(i, j, k - 1, this.id);
        world.notifyBlocksOfNeighborChange(i, j, k + 1, this.id);
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this.id);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this.id);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this.id);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityCapacitor(capacity);
    }
}

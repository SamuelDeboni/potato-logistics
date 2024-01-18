package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityCapacitor;
import net.minecraft.core.block.BlockLever;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockCapacitor extends BlockTileEntity {

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
        return needPower;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        return this.isPoweringTo(world, x, y, z, side);
    }

    @Override
    public int tickRate() {
        return 20;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this.id);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this.id);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this.id);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this.id);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        PotatoLogisticsMod.LOGGER.info("New block entity");
        return new TileEntityCapacitor(100000);
    }
}

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

    public BlockCapacitor(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }
    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntityCapacitor tile = (TileEntityCapacitor) blockAccess.getBlockTileEntity(x, y, z);
        PotatoLogisticsMod.LOGGER.info("need power: " + tile.needPower);
        return tile != null && !tile.needPower;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        TileEntityCapacitor tile = (TileEntityCapacitor) world.getBlockTileEntity(x, y, z);
        PotatoLogisticsMod.LOGGER.info("need power 2: " + tile.needPower);
        return tile != null && !tile.needPower;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityCapacitor();
    }
}

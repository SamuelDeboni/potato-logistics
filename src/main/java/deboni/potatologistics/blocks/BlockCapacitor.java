package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityCapacitor;
import net.minecraft.client.util.helper.Colors;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Color;
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
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return meta == 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        return isPoweringTo(world, x, y, z, side);
    }

    public static void notifyNeighbors(World world, int x, int y, int z, int id) {
        world.notifyBlocksOfNeighborChange(x, y - 1, z, id);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, id);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, id);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, id);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, id);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, id);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        notifyNeighbors(world, x, y, z, this.id);
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        super.onBlockRemoved(world, x, y, z, data);

        notifyNeighbors(world, x, y, z, this.id);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityCapacitor();
    }
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        this.spawnParticles(world, x, y, z);
    }

    private void spawnParticles(World world, int x, int y, int z) {
        Random random = world.rand;
        Color color = new Color();
        color.setRGBA(50, 255, 0, 255);

        int rate = 0;
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityCapacitor) {
            TileEntityCapacitor c = (TileEntityCapacitor) te;
            rate = (int)(((float)c.energy / c.capacity) * 4);
        }

        float fRate = (float) rate / 4.0f;

        float red = 0.2f * fRate;
        float green = fRate;
        float blue = 0.0f;
        double d = 0.0625;
        for (int i = 0; i < rate; ++i) {
            double px = (float)x + random.nextFloat();
            double py = (float)y + random.nextFloat();
            double pz = (float)z + random.nextFloat();
            if (i == 0 && !world.isBlockOpaqueCube(x, y + 1, z)) {
                py = (double)(y + 1) + d;
            }
            if (i == 1 && !world.isBlockOpaqueCube(x, y - 1, z)) {
                py = (double)(y + 0) - d;
            }
            if (i == 2 && !world.isBlockOpaqueCube(x, y, z + 1)) {
                pz = (double)(z + 1) + d;
            }
            if (i == 3 && !world.isBlockOpaqueCube(x, y, z - 1)) {
                pz = (double)(z + 0) - d;
            }
            if (i == 4 && !world.isBlockOpaqueCube(x + 1, y, z)) {
                px = (double)(x + 1) + d;
            }
            if (i == 5 && !world.isBlockOpaqueCube(x - 1, y, z)) {
                px = (double)(x + 0) - d;
            }
            if (!(px < (double)x || px > (double)(x + 1) || py < 0.0 || py > (double)(y + 1) || pz < (double)z) && !(pz > (double)(z + 1))) continue;
            world.spawnParticle("reddust", px, py, pz, red, green, blue);
        }
    }
}

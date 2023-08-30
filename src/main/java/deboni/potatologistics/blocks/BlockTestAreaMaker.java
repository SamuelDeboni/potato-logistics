package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockTestAreaMaker extends Block {
    public BlockTestAreaMaker(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        super.onBlockPlaced(world, x, y, z, side, entity, sideHeight);

        int startX = world.getChunkFromBlockCoords(x, z).xPosition * 16;
        int startZ = world.getChunkFromBlockCoords(x, z).zPosition * 16;
        int endX = startX + 15;
        int endZ = startZ + 15;

        for (int yi = y + 1; yi < 256; yi++) {
            for (int zi = startZ; zi <= endZ; zi++) {
                for (int xi = startX; xi <= endX; xi++) {
                    world.setBlockWithNotify(xi, yi, zi, 0);
                }
            }
        }

        for (int zi = startZ; zi <= endZ; zi++) {
            for (int xi = startX; xi <= endX; xi++) {
                world.setBlockWithNotify(xi, y, zi, (zi & 1) == (zi & 1) ? Block.basaltPolished.id : Block.marble.id);
            }
        }
    }
}

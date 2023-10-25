package deboni.potatologistics.blocks;

import deboni.potatologistics.PipeStack;
import deboni.potatologistics.Util;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockBlockPlacer extends BlockRotatable {
    public BlockBlockPlacer(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId > 0 && Block.blocksList[blockId].canProvidePower()) {
            boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
            if (flag) {
                world.scheduleBlockUpdate(x, y, z, this.id, 0);
            }
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        Direction dir = entity.getPlacementDirection(side);
        if (dir == Direction.UP || dir == Direction.DOWN) dir = dir.getOpposite();
        if (!entity.isSneaking()) dir = dir.getOpposite();
        world.setBlockMetadataWithNotify(x, y, z, dir.getId());
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
        this.setDefaultDirection(world, i, j, k);
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        super.onBlockRemoval(world, x, y, z);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
            int meta = world.getBlockMetadata(x, y, z);
            Direction dir = Direction.getDirectionById(BlockRotatable.getOrientation(meta)).getOpposite();
            if (dir != Direction.UP && dir != Direction.DOWN) dir = dir.getOpposite();

            int ix = x - dir.getOffsetX();
            int iy = y - dir.getOffsetY();
            int iz = z - dir.getOffsetZ();

            TileEntity inTe = world.getBlockTileEntity(ix, iy, iz) ;

            int tx = x + dir.getOffsetX();
            int ty = y + dir.getOffsetY();
            int tz = z + dir.getOffsetZ();

            Block block = world.getBlock(tx, ty, tz);
            if (block != null) return;

            ItemStack blockToPlace = null;

            if (inTe instanceof IInventory) {
                PipeStack pipeStack = Util.getItemFromInventory(world, ix, iy, iz, dir, 2);
                if (pipeStack != null) blockToPlace = pipeStack.stack;
            } else if (inTe instanceof TileEntityPipe) {
                // TODO
            }

            if (blockToPlace == null || blockToPlace.itemID >= Block.blocksList.length) return;

            world.playSoundEffect(2000, tx, ty, tz, blockToPlace.itemID);
            boolean placed = world.setBlockWithNotify(tx, ty, tz, blockToPlace.itemID);

        }
    }
}

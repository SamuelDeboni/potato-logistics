package deboni.potatologistics.blocks;

import deboni.potatologistics.Util;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
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
import net.minecraft.core.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockTreeChopper extends BlockRotatable {
    public BlockTreeChopper(String key, int id, Material material) {
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

    private List<ItemStack> breakBlock(World world, int x, int y, int z, int depth) {
        ItemStack[] breakResult = null;

        if (depth > 128) return null;

        Block block = world.getBlock(x, y, z);
        if (block == null) return null;

        if (block instanceof BlockLog) {
            breakResult = new ItemStack[1];
            breakResult[0] = new ItemStack(block.asItem());
        } else if (block instanceof BlockLeavesBase) {
            //breakResult = block.getBreakResult(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), null);
            block.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), null);
        } else {
            return null;
        }

        world.playSoundEffect(2001, x, y, z, block.id);
        boolean removed = world.setBlockWithNotify(x, y, z, 0);

        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.getDirectionById(i);
            List<ItemStack> res = breakBlock(world, x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), depth + 1);
            if (res != null && res.size() > 0) {
                list.addAll(res);
            }
        }

        if (removed && breakResult != null && breakResult.length > 0) {
            for (ItemStack r: breakResult) list.add(r.copy());
            return list;
        }

        return null;
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

            int tx = x + dir.getOffsetX();
            int ty = y + dir.getOffsetY();
            int tz = z + dir.getOffsetZ();

            List<ItemStack> breakResult = this.breakBlock(world, tx, ty, tz, 0);
            if (breakResult == null) return;

            TileEntity outTe = world.getBlockTileEntity(ix, iy, iz) ;

            if (breakResult != null && breakResult.size() > 0) {
                if (outTe instanceof IInventory) {
                    IInventory inventory;
                    if (outTe instanceof TileEntityChest) {
                        inventory = BlockChest.getInventory(world, ix, iy, iz);
                    } else {
                        inventory = (IInventory) outTe;
                    }
                    if (inventory != null) {
                        for (ItemStack stack : breakResult) {
                            boolean hasInserted = Util.insertOnInventory(inventory, stack, dir, new TileEntityPipe[0]);
                            if (!hasInserted) return;
                        }
                    }
                } else {
                    for (ItemStack stack : breakResult) {
                        world.dropItem(ix, iy, iz, stack);
                    }
                }
            }
        }
    }
}

package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.client.sound.SoundPoolEntry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.BlockRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockBlockBreaker extends BlockRotatable {
    public BlockBlockBreaker(String key, int id, Material material) {
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
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z)) {
            int meta = world.getBlockMetadata(x, y, z);
            Direction dir = Direction.getDirectionById(BlockRotatable.getOrientation(meta));
            if (dir != Direction.UP && dir != Direction.DOWN) dir = dir.getOpposite();


            int ix = x - dir.getOffsetX();
            int iy = y - dir.getOffsetY();
            int iz = z - dir.getOffsetZ();

            TileEntity outTe = world.getBlockTileEntity(ix, iy, iz) ;

            int tx = x + dir.getOffsetX();
            int ty = y + dir.getOffsetY();
            int tz = z + dir.getOffsetZ();

            Block block = world.getBlock(tx, ty, tz);
            if (block == null) return;

            int tmeta = world.getBlockMetadata(tx, ty, tz);
            TileEntity te = world.getBlockTileEntity(tx, ty ,tz);

            ItemStack[] breakResult;
            //if (te != null) {
            breakResult = block.getBreakResult(world, EnumDropCause.PROPER_TOOL, tx, ty, tz, tmeta, te);
            //} else {
                //breakResult = new ItemStack[1];
                //breakResult[0] = new ItemStack(block.asItem());
            //}

            if (breakResult != null && breakResult.length > 0) {
                if (outTe instanceof IInventory) {
                    IInventory inventory;
                    if (outTe instanceof TileEntityChest) {
                        inventory = BlockChest.getInventory(world, ix, iy, iz);
                    } else {
                        inventory = (IInventory) outTe;
                    }
                    if (inventory != null) {
                        for (ItemStack stack : breakResult) {
                            boolean hasInserted = TileEntityPipe.insertOnInventory(inventory, stack, dir, new TileEntityPipe[0]);
                            if (!hasInserted) return;
                        }
                    }
                } else if (outTe instanceof TileEntityPipe) {
                    TileEntityPipe pipe = (TileEntityPipe) outTe;
                    if (breakResult.length > 1) return;
                    boolean hasAdded = pipe.addToStack(breakResult[0], dir);
                    if (!hasAdded) return;
                } else {
                    for (ItemStack stack : breakResult) {
                        world.dropItem(ix, iy, iz, stack);
                    }
                }
            }

            world.playSoundEffect(2001, tx, ty, tz, block.id);
            boolean removed = world.setBlockWithNotify(tx, ty, tz, 0);
            if (removed) {
                //block.onBlockRemoval(world, tx, ty, tz);
            }
        }
    }
}

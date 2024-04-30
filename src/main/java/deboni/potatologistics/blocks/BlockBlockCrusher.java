package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.Util;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.piston.BlockPistonBase;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.HashMap;
import java.util.Random;

public class BlockBlockCrusher extends BlockRotatable {
    public static HashMap<Block, ItemStack[]> crushResults = new HashMap<>();
    static {
        crushResults.put(Block.cobbleStone, new ItemStack[]{new ItemStack(Block.gravel)});
        crushResults.put(Block.cobbleBasalt, new ItemStack[]{new ItemStack(Block.gravel)});
        crushResults.put(Block.cobbleGranite, new ItemStack[]{new ItemStack(Block.gravel)});
        crushResults.put(Block.cobbleLimestone, new ItemStack[]{new ItemStack(Block.gravel)});
        crushResults.put(Block.cobbleStoneMossy, new ItemStack[]{new ItemStack(Block.gravel)});
        crushResults.put(Block.gravel, new ItemStack[]{new ItemStack(Block.sand)});
        crushResults.put(Block.dirt, new ItemStack[]{new ItemStack(Item.ammoPebble), new ItemStack(Item.ammoPebble)});
    }
    public BlockBlockCrusher(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        this.checkIfAction(world, x, y, z);
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
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        super.onBlockRemoved(world, x, y, z, data);
    }

    public static boolean isPowered(int data) {
        return (data & 8) != 0;
    }

    private void checkIfAction(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        Direction dir = Direction.getDirectionById(BlockRotatable.getOrientation(meta & 7));
        boolean hasNeighborSignal = this.getNeighborSignal(world, x, y, z, dir.getId());

        if (hasNeighborSignal) {
            if (!isPowered(meta)) world.triggerEvent(x, y, z, 0, dir.getId());
            meta = dir.getId();
            meta |= 8;
        } else {
            meta = dir.getId();
        }
        world.setBlockMetadata(x, y, z, meta);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(Side side, int data) {
        return super.getBlockTextureFromSideAndMetadata(side, data & 7);
    }

    private boolean getNeighborSignal(World world, int x, int y, int z, int direction) {
        if (direction != 0 && world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0)) {
            return true;
        }
        if (direction != 1 && world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1)) {
            return true;
        }
        if (direction != 2 && world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2)) {
            return true;
        }
        if (direction != 3 && world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3)) {
            return true;
        }
        if (direction != 5 && world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5)) {
            return true;
        }
        if (direction != 4 && world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4)) {
            return true;
        }
        if (world.isBlockIndirectlyProvidingPowerTo(x, y, z, 0)) {
            return true;
        }
        if (world.isBlockIndirectlyProvidingPowerTo(x, y + 2, z, 1)) {
            return true;
        }
        if (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z - 1, 2)) {
            return true;
        }
        if (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z + 1, 3)) {
            return true;
        }
        if (world.isBlockIndirectlyProvidingPowerTo(x - 1, y + 1, z, 4)) {
            return true;
        }
        return world.isBlockIndirectlyProvidingPowerTo(x + 1, y + 1, z, 5);
    }

    @Override
    public void triggerEvent(World world, int x, int y, int z, int index, int meta) {
        Direction dir = Direction.getDirectionById(BlockRotatable.getOrientation(meta));
        if (dir == Direction.UP || dir == Direction.DOWN) dir = dir.getOpposite();

        int ix = x - dir.getOffsetX();
        int iy = y - dir.getOffsetY();
        int iz = z - dir.getOffsetZ();

        TileEntity outTe = Util.getBlockTileEntity(world, ix, iy, iz) ;

        int tx = x + dir.getOffsetX();
        int ty = y + dir.getOffsetY();
        int tz = z + dir.getOffsetZ();

        Block block = world.getBlock(tx, ty, tz);
        if (block == null) return;

        int tmeta = world.getBlockMetadata(tx, ty, tz);
        TileEntity te = Util.getBlockTileEntity(world, tx, ty ,tz);

        boolean breakBlock = false;
        ItemStack[] breakResult = crushResults.get(block);
        if (breakResult != null){
            breakResult = Util.cloneStackArray(breakResult);
            breakBlock = true;
        } else if (block.getHardness() >= 0){
            breakResult = block.getBreakResult(world, EnumDropCause.PROPER_TOOL, tx, ty, tz, tmeta, te);
            breakBlock = true;
        }

        if (!world.isClientSide && breakResult != null && breakResult.length > 0) {
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
        if (breakBlock){
            world.playSoundEffect(2001, tx, ty, tz, block.id);
            world.setBlockWithNotify(tx, ty, tz, 0);
        }
    }
}

package deboni.potatologistics;

import deboni.potatologistics.blocks.entities.TileEntityAutoBascket;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.IItemIO;

import java.util.Objects;

public class Util {

    public static PipeStack getItemFromInventory(World world, int x, int y, int z, Direction dir, int stackTimer) {
        PipeStack returnStack = null;

        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof IItemIO && te instanceof IInventory) {
            sunsetsatellite.sunsetutils.util.Direction sdir = sunsetsatellite.sunsetutils.util.Direction.getDirectionFromSide(dir.getId()).getOpposite();
            IItemIO itemIo = (IItemIO) te;
            IInventory inventory = (IInventory) itemIo;

            Connection con = itemIo.getItemIOForSide(sdir);
            if (con == Connection.OUTPUT || con == Connection.BOTH) {
                int index = itemIo.getActiveItemSlotForSide(sdir);

                ItemStack stack = inventory.getStackInSlot(index);
                if (stack != null) {
                    stack.stackSize--;
                    returnStack = new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), dir, stackTimer);
                    if (stack.stackSize <= 0) stack = null;
                    inventory.setInventorySlotContents(index, stack);
                }
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            String inventoryName = inventory.getInvName();

            if (Objects.equals(inventoryName, "Chest")) {
                inventory = BlockChest.getInventory(world, x, y ,z);
            }

            if (Objects.equals(inventoryName, "Chest")
                    || Objects.equals(inventoryName, "Trap")
                    || Objects.equals(inventoryName, "Filter")
            ) {
                int inventorySize = inventory.getSizeInventory();
                ItemStack stack = null;
                int j = 0;

                if (inventoryName != "Filter") {
                    for (; stack == null && j < inventorySize; j++) stack = inventory.getStackInSlot(j);
                } else {
                    for (; stack == null && j < inventorySize; j++) {
                        stack = inventory.getStackInSlot(j);
                        if (stack != null && stack.stackSize <= 1) stack = null;
                    }
                }

                if (stack != null && j > 0) {
                    stack.stackSize--;

                    returnStack = new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), dir, stackTimer);
                    if (stack.stackSize <= 0) stack = null;
                    inventory.setInventorySlotContents(j - 1, stack);
                    return returnStack;
                }
            } else if (Objects.equals(inventoryName, "Trommel")) {
                int inventorySize = 4;
                ItemStack stack = null;
                int j = 0;
                for (; stack == null && j < inventorySize; j++) stack = inventory.getStackInSlot(j);

                if (stack != null && j > 0) {
                    stack.stackSize--;
                    returnStack = new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), dir, stackTimer);
                    if (stack.stackSize <= 0) stack = null;
                    inventory.setInventorySlotContents(j - 1, stack);
                    return returnStack;
                }
            } else {
                ItemStack stack = inventory.getStackInSlot(2);
                if (stack != null) {
                    stack.stackSize--;
                    returnStack = new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), dir, stackTimer);
                    if (stack.stackSize <= 0) stack = null;
                    inventory.setInventorySlotContents(2, stack);
                }
            }
        } else if (te instanceof TileEntityAutoBascket && dir == Direction.UP) {
            ItemStack stack = ((TileEntityAutoBascket)te).removeOneItem();
            if (stack != null) {
                returnStack = new PipeStack(stack, dir, stackTimer);
            }
        }

        return returnStack;
    }

    public static boolean insertOnInventory(IInventory inventory, ItemStack stack, Direction direction, TileEntityPipe[] pipes) {
        boolean hasInserted = false;
        int inventorySize = inventory.getSizeInventory();
        String inventoryName = inventory.getInvName();


        if (inventoryName == "Chest" || inventoryName == "Large Chest" || inventoryName == "Trap" || inventoryName == "Filter") {
            int j = 0;
            ItemStack chestStack;
            while (j < inventorySize) {
                chestStack = inventory.getStackInSlot(j);

                if (chestStack == null) {
                    if (inventoryName != "Filter") {
                        inventory.setInventorySlotContents(j, stack);
                        hasInserted = true;
                    }
                    break;
                }

                if (chestStack.itemID == stack.itemID && chestStack.stackSize < chestStack.getMaxStackSize()) {
                    chestStack.stackSize++;
                    inventory.setInventorySlotContents(j, chestStack);

                    hasInserted = true;
                    break;
                }

                j++;
            }
        } else {
            int fuelSlot = 1;
            int inputSlot = 0;

            if (inventoryName == "Trommel") {
                fuelSlot = 4;
                for (; inputSlot < 3; inputSlot++) {
                    ItemStack s = inventory.getStackInSlot(inputSlot);
                    if (s == null || s.itemID == stack.itemID && s.stackSize < s.getMaxStackSize()) break;
                }
            }

            int targetSlot = direction == Direction.UP ? fuelSlot : inputSlot;

            ItemStack furnaceStack = inventory.getStackInSlot(targetSlot);

            if (furnaceStack == null) {
                inventory.setInventorySlotContents(targetSlot, stack);
                hasInserted = true;
            } else {
                int maxStackSize = furnaceStack.getMaxStackSize();
                if (pipes.length > 0) {
                    maxStackSize = Math.min(maxStackSize, 8);
                }

                if (furnaceStack.itemID == stack.itemID && furnaceStack.stackSize < maxStackSize) {
                    furnaceStack.stackSize++;
                    inventory.setInventorySlotContents(targetSlot, furnaceStack);
                    hasInserted = true;
                }
            }
        }

        return  hasInserted;
    }
}

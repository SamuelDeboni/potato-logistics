package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockBasket;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBasket;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import sunsetsatellite.sunsetutils.util.IItemIO;

import java.util.*;

class PipeStack {
    public ItemStack stack;
    public Direction direction;
    public int timer = 0;

    public PipeStack(ItemStack stack, Direction direction) {
        this.stack = stack;
        this.direction = direction;
        this.timer = 5;
    }

    public PipeStack() {
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        nbttagcompound.putShort("direction", (short)this.direction.getId());
        nbttagcompound.putShort("timer", (short)this.timer);
        stack.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        this.direction = Direction.getDirectionById(nbttagcompound.getShort("direction"));
        this.timer = nbttagcompound.getShort("timer");
        this.stack = ItemStack.readItemStackFromNbt(nbttagcompound);
    }

    public static PipeStack readPipeStackFromNbt(CompoundTag nbt) {
        if (nbt == null) {
            return null;
        }
        PipeStack stack = new PipeStack();
        stack.readFromNBT(nbt);
        return stack;
    }
}

public class TileEntityPipe extends TileEntity {
    public List<PipeStack> stacks;
    public int timer = 5;
    public int stackLimit = 1;
    public TileEntityPipe() {
        stacks = new ArrayList<>(16);
    }

    private static final int[][] offsets = {
            { 0, -1,  0},
            { 0,  1,  0},
            { 0,  0, -1},
            { 0,  0,  1},
            {-1,  0,  0},
            { 1,  0,  0},
    };

    public void dropItems() {
        for (PipeStack stack : stacks) {
            worldObj.dropItem(xCoord, yCoord, zCoord, stack.stack);
        }
    }

    public List<ItemStack> getStacksInPipe() {
        List<ItemStack> l = new ArrayList<>(this.stacks.size());
        for (PipeStack stack : stacks) {
            l.add(stack.stack);
        }
        return l;
    }

    public boolean addToStack(ItemStack stack, Direction dir) {
        if (!this.stacks.isEmpty()) return false;
        this.stacks.add(new PipeStack(stack, dir));
        return true;
    }

    public List<float[]> getStacksInPipePosition() {
        List<float[]> l = new ArrayList<>(this.stacks.size());
        for (PipeStack stack : stacks) {
            float[] pos = new float[3];
            float xof = stack.direction.getOffsetX();
            float yof = stack.direction.getOffsetY();
            float zof = stack.direction.getOffsetZ();

            pos[0] = 0.5f + xof * (stack.timer / 5.0f);
            pos[1] = 0.5f + yof * (stack.timer / 5.0f);
            pos[2] = 0.5f + zof * (stack.timer / 5.0f);
            l.add(pos);
        }
        return l;
    }

    public boolean isDirectional() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return (meta & (1 << 2)) != 0;
    }

    public Direction getDirection() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return Direction.getDirectionById(meta >> 3);
    }
    public boolean isPointingTo(int x, int y, int z) {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        Direction dir = Direction.getDirectionById(meta >> 3);
        boolean sameX = dir.getOffsetX() == xCoord - x;
        boolean sameY = dir.getOffsetY() == yCoord - y;
        boolean sameZ = dir.getOffsetZ() == zCoord - z;
        return sameX && sameY && sameZ;
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("Items");
        this.stacks = new ArrayList<>();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.tagAt(i);
            this.stacks.add(PipeStack.readPipeStackFromNbt(nbttagcompound1));
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < this.stacks.size(); i++) {
            if (this.stacks.get(i) == null) continue;
            CompoundTag nbttagcompound1 = new CompoundTag();
            this.stacks.get(i).writeToNBT(nbttagcompound1);
            nbttaglist.addTag(nbttagcompound1);
        }
        nbttagcompound.put("Items", nbttaglist);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        timer--;

        if (!stacks.isEmpty()) worldObj.markBlockNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);
          //if (timer > 0) return;

        timer = 5;

        int meta = getBlockMetadata();

        int type = meta & 0x03;
        boolean isDirectional = (meta & (1 << 2)) != 0;
        Direction pipeDirection = Direction.getDirectionById(meta >> 3).getOpposite();

        if (type == 1 && stacks.size() < this.stackLimit) {
            for (int i = 0; i < offsets.length; i++) {
                int x = xCoord + offsets[i][0];
                int y = yCoord + offsets[i][1];
                int z = zCoord + offsets[i][2];
                int nid = worldObj.getBlockId(x, y, z);

                TileEntity te = worldObj.getBlockTileEntity(x, y, z);
                if (te instanceof IItemIO) {

                } else if (te instanceof IInventory) {
                    IInventory inventory = (IInventory) te;
                    String inventoryName = inventory.getInvName();

                    if (Objects.equals(inventoryName, "Chest")) {
                        inventory = BlockChest.getInventory(worldObj, x, y ,z);
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
                            this.stacks.add(new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), Direction.getDirectionById(i)));
                            if (stack.stackSize <= 0) stack = null;
                            inventory.setInventorySlotContents(j - 1, stack);
                            break;
                        }
                    } else if (Objects.equals(inventoryName, "Trommel")) {
                        int inventorySize = 4;
                        ItemStack stack = null;
                        int j = 0;
                        for (; stack == null && j < inventorySize; j++) stack = inventory.getStackInSlot(j);

                        if (stack != null && j > 0) {
                            stack.stackSize--;
                            this.stacks.add(new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), Direction.getDirectionById(i)));
                            if (stack.stackSize <= 0) stack = null;
                            inventory.setInventorySlotContents(j - 1, stack);
                            break;
                        }
                    } else {
                        ItemStack stack = inventory.getStackInSlot(2);
                        if (stack != null) {
                            stack.stackSize--;
                            this.stacks.add(new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), Direction.getDirectionById(i)));
                            if (stack.stackSize <= 0) stack = null;
                            inventory.setInventorySlotContents(2, stack);
                        }
                    }
                } else if (te instanceof TileEntityAutoBascket && Direction.getDirectionById(i) == Direction.UP) {
                    ItemStack stack = ((TileEntityAutoBascket)te).removeOneItem();
                    if (stack != null) {
                        this.stacks.add(new PipeStack(stack, Direction.getDirectionById(i)));
                    }
                }
            }
        }

        for (int stackindex = 0; stackindex < stacks.size(); stackindex++) {
            PipeStack stack = stacks.get(stackindex);
            if (stack.timer <= 0) {
                List<TileEntityPipe> pipes = new ArrayList<>(6);
                List<IInventory> inventories = new ArrayList<>(6);
                //List<IInventory> furnaces = new ArrayList<>(6);
                List<Direction> inventoriesDirection = new ArrayList<>(6);
                List<Direction> directions = new ArrayList<>(6);

                for (int i = 0; i < offsets.length; i++) {
                    int x = xCoord + offsets[i][0];
                    int y = yCoord + offsets[i][1];
                    int z = zCoord + offsets[i][2];
                    int nid = worldObj.getBlockId(x, y, z);

                    TileEntity te = worldObj.getBlockTileEntity(x, y, z);

                    if (te instanceof TileEntityPipe && stack.direction != Direction.getDirectionById(i)) {
                        TileEntityPipe pipe = (TileEntityPipe) te;
                        if (pipe.stacks.size() < pipe.stackLimit && (!isDirectional || pipeDirection.getId() == i)) {
                            pipes.add(pipe);
                            directions.add(Direction.getDirectionById(i));
                        }
                    } else if (te instanceof IInventory) {
                        IInventory inventory = (IInventory) te;

                        if (inventory instanceof TileEntityChest) {
                            inventory = BlockChest.getInventory(worldObj, x, y ,z);
                        }

                        if (type == 2) {
                            inventories.add(inventory);
                            inventoriesDirection.add(Direction.getDirectionById(i));
                        }
                    }
                }

                boolean hasInsertedInChest = false;
                if (!inventories.isEmpty()) {
                    int idx = (int)(Math.random() * (double) inventories.size());
                    IInventory inventory = inventories.get(idx);
                    TileEntityPipe[] pipesArray = new TileEntityPipe[pipes.size()];
                    for (int i = 0; i < pipes.size(); i++) pipesArray[i] = pipes.get(i);
                    hasInsertedInChest = insertOnInventory(inventory, stack.stack, inventoriesDirection.get(idx), pipesArray);
                }


                if (!hasInsertedInChest) {
                    if (!pipes.isEmpty()) {
                        int idx = (int) (Math.random() * (double) pipes.size());
                        TileEntityPipe pipe = pipes.get(idx);
                        stack.timer = 5;
                        stack.direction = directions.get(idx).getOpposite();
                        pipe.stacks.add(stack);
                    } else {
                        continue;
                    }
                }

                stacks.remove(stack);
                stackindex--;
            } else {
                stack.timer--;
            }
        }
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

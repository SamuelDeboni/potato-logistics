package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;

import java.util.ArrayList;
import java.util.List;

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
    private List<PipeStack> stacks;
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

                if(nid == Block.chestPlanksOak.id || nid == Block.chestPlanksOakPainted.id) {
                    IInventory inventory = BlockChest.getInventory(worldObj, x, y, z);
                    int inventorySize = inventory.getSizeInventory();
                    ItemStack stack = null;
                    int j = 0;
                    for (; stack == null && j < inventorySize; j++) stack = inventory.getStackInSlot(j);

                    if (stack != null && j > 0) {
                        stack.stackSize--;
                        this.stacks.add(new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), Direction.getDirectionById(i)));
                        if (stack.stackSize <= 0) stack = null;
                        inventory.setInventorySlotContents(j-1, stack);
                        break;
                    }
                } else {
                    TileEntity te = worldObj.getBlockTileEntity(x, y, z);
                    if (te instanceof IInventory) {
                        IInventory inventory = (IInventory) te;

                        ItemStack stack = inventory.getStackInSlot(2);
                        if (stack != null) {
                            stack.stackSize--;
                            this.stacks.add(new PipeStack(new ItemStack(stack.getItem(), 1, stack.getMetadata()), Direction.getDirectionById(i)));
                            if (stack.stackSize <= 0) stack = null;
                            inventory.setInventorySlotContents(2, stack);
                        }
                    }
                }
            }
        }

        for (int stackindex = 0; stackindex < stacks.size(); stackindex++) {
            PipeStack stack = stacks.get(stackindex);
            if (stack.timer <= 0) {
                List<TileEntityPipe> pipes = new ArrayList<>(6);
                List<IInventory> inventories = new ArrayList<>(6);

                List<IInventory> furnaces = new ArrayList<>(6);
                List<Direction> furnacesDirection = new ArrayList<>(6);

                List<Direction> directions = new ArrayList<>(6);
                for (int i = 0; i < offsets.length; i++) {
                    int x = xCoord + offsets[i][0];
                    int y = yCoord + offsets[i][1];
                    int z = zCoord + offsets[i][2];
                    int nid = worldObj.getBlockId(x, y, z);

                    TileEntity te = worldObj.getBlockTileEntity(x, y, z);
                    if (te instanceof TileEntityPipe && stack.direction != Direction.getDirectionById(i)) {
                        TileEntityPipe pipe = (TileEntityPipe)te;
                        if (pipe.stacks.size() < pipe.stackLimit && (!isDirectional || pipeDirection.getId() == i)) {
                            pipes.add(pipe);
                            directions.add(Direction.getDirectionById(i));
                        }
                    }

                    if (type == 2)  {
                        if (nid == Block.chestPlanksOak.id || nid == Block.chestPlanksOakPainted.id) {
                            IInventory inventory = BlockChest.getInventory(worldObj, x, y, z);
                            if (inventory != null) {
                                inventories.add(inventory);
                            }
                        } else {
                            TileEntity te2 = worldObj.getBlockTileEntity(x, y, z);
                            if (te2 instanceof IInventory) {
                                IInventory inventory = (IInventory) te2;
                                furnaces.add(inventory);
                                furnacesDirection.add(Direction.getDirectionById(i));
                            }
                        }
                    }
                }

                boolean hasInsertedInChest = false;
                if (!inventories.isEmpty()) {
                    int idx = (int)(Math.random() * (double) inventories.size());

                    IInventory inventory = inventories.get(idx);
                    int inventorySize = inventory.getSizeInventory();
                    int j = 0;
                    ItemStack chestStack;
                    while (j < inventorySize) {
                        chestStack = inventory.getStackInSlot(j);

                        if (chestStack == null) {
                            inventory.setInventorySlotContents(j, stack.stack);
                            hasInsertedInChest = true;
                            break;
                        }

                        if (chestStack.itemID == stack.stack.itemID && chestStack.stackSize < chestStack.getMaxStackSize()) {
                            chestStack.stackSize++;
                            inventory.setInventorySlotContents(j, chestStack);

                            hasInsertedInChest = true;
                            break;
                        }

                        j++;
                    }
                }

                if (!hasInsertedInChest && !furnaces.isEmpty()) {
                    int idx = (int)(Math.random() * (double) furnaces.size());

                    IInventory furnace = furnaces.get(idx);
                    Direction direction = furnacesDirection.get(idx);

                    int targetSlot = direction == Direction.UP ? 1 : 0;

                    ItemStack furnaceStack = furnace.getStackInSlot(targetSlot);

                    if (furnaceStack == null) {
                        furnace.setInventorySlotContents(targetSlot, stack.stack);
                        hasInsertedInChest = true;
                    } else {
                        int maxStackSize = furnaceStack.getMaxStackSize();
                        if (!pipes.isEmpty()) {
                            maxStackSize = Math.min(maxStackSize, 8);
                        }

                        if (furnaceStack.itemID == stack.stack.itemID && furnaceStack.stackSize < maxStackSize) {
                            furnaceStack.stackSize++;
                            furnace.setInventorySlotContents(targetSlot, furnaceStack);
                            hasInsertedInChest = true;
                        }
                    }
                }

                if (!hasInsertedInChest) {
                    if (!pipes.isEmpty()) {
                        int idx = (int) (Math.random() * (double) pipes.size());
                        TileEntityPipe pipe = pipes.get(idx);
                        stack.timer = 5;
                        stack.direction = directions.get(idx).getOpposite();
                        pipe.stacks.add(stack);
                    } else {
                        Direction d = stack.direction.getOpposite();
                        int xOff = d.getOffsetX();
                        int yOff = d.getOffsetY();
                        int zOff = d.getOffsetZ();
                        //worldObj.dropItem(xCoord + xOff, yCoord + yOff, zCoord + zOff, stack.stack);
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
}

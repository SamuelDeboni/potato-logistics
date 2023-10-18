package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.PipeStack;
import deboni.potatologistics.Util;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.IItemIO;

import java.util.*;

public class TileEntityPipe extends TileEntity {
    public List<PipeStack> stacks;
    public int stackTimer = 10;
    public TileEntityPipe() {
        stacks = new ArrayList<>(16);
    }

    private int timer = 0;
    public int timerLen = 4;
    public int stackLimit = 4;

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
        this.stacks.add(new PipeStack(stack, dir, stackTimer));
        return true;
    }

    public List<float[]> getStacksInPipePosition() {
        List<float[]> l = new ArrayList<>(this.stacks.size());
        for (PipeStack stack : stacks) {
            float[] pos = new float[3];
            float xof = stack.direction.getOffsetX();
            float yof = stack.direction.getOffsetY();
            float zof = stack.direction.getOffsetZ();

            pos[0] = 0.5f + xof * (stack.timer / (float)stackTimer);
            pos[1] = 0.5f + yof * (stack.timer / (float)stackTimer);
            pos[2] = 0.5f + zof * (stack.timer / (float)stackTimer);
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

        if (!stacks.isEmpty()) worldObj.markBlockNeedsUpdate(this.xCoord, this.yCoord, this.zCoord);

        int meta = getBlockMetadata();

        int type = meta & 0x03;
        boolean isDirectional = (meta & (1 << 2)) != 0;
        Direction pipeDirection = Direction.getDirectionById(meta >> 3).getOpposite();

        if (type == 1 && stacks.size() < this.stackLimit && timer <= 0) {
            for (int i = 0; i < offsets.length; i++) {
                int x = xCoord + offsets[i][0];
                int y = yCoord + offsets[i][1];
                int z = zCoord + offsets[i][2];

                PipeStack inventoryStack = Util.getItemFromInventory(worldObj, x, y, z, Direction.getDirectionById(i), this.stackTimer);
                if (inventoryStack != null) {
                    stacks.add(inventoryStack);
                    break;
                }
            }

            timer = timerLen;
        }

        timer--;

        for (int stackindex = 0; stackindex < stacks.size(); stackindex++) {
            PipeStack stack = stacks.get(stackindex);
            if (stack == null || stack.stack == null) {
                stacks.remove(stackindex);
                stackindex--;
                continue;
            }

            if (stack.timer <= 0) {
                List<TileEntityPipe> pipes = new ArrayList<>(6);
                List<IInventory> inventories = new ArrayList<>(6);

                List<IInventory> ioInventories = new ArrayList<>(6);
                List<IItemIO> itemIOs = new ArrayList<>(6);
                List<sunsetsatellite.sunsetutils.util.Direction> ioDirections = new ArrayList<>(6);

                //List<IInventory> furnaces = new ArrayList<>(6);
                List<Direction> inventoriesDirection = new ArrayList<>(6);
                List<Direction> directions = new ArrayList<>(6);

                for (int i = 0; i < offsets.length; i++) {
                    int x = xCoord + offsets[i][0];
                    int y = yCoord + offsets[i][1];
                    int z = zCoord + offsets[i][2];
                    int nid = worldObj.getBlockId(x, y, z);

                    TileEntity te = worldObj.getBlockTileEntity(x, y, z);

                    if (te instanceof IItemIO && te instanceof IInventory) {
                        sunsetsatellite.sunsetutils.util.Direction sdir = sunsetsatellite.sunsetutils.util.Direction.getDirectionFromSide(i).getOpposite();
                        IItemIO itemIo = (IItemIO) te;
                        IInventory inventory = (IInventory) itemIo;

                        Connection con = itemIo.getItemIOForSide(sdir);
                        if (con == Connection.INPUT || con == Connection.BOTH) {
                            ioInventories.add(inventory);
                            itemIOs.add(itemIo);
                            ioDirections.add(sdir);
                        }
                    } else if (te instanceof TileEntityPipe && stack.direction != Direction.getDirectionById(i)) {
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
                if (!ioInventories.isEmpty()) {
                    int idx = (int)(Math.random() * (double) ioInventories.size());
                    IInventory inventory = ioInventories.get(idx);
                    sunsetsatellite.sunsetutils.util.Direction dir = ioDirections.get(idx);
                    IItemIO io = itemIOs.get(idx);
                    int stackIdx = io.getActiveItemSlotForSide(dir);

                    ItemStack s = inventory.getStackInSlot(stackIdx);
                    if (s != null) {
                        int maxStackSize = s.getMaxStackSize();

                        if (s.itemID == stack.stack.itemID && s.stackSize < maxStackSize) {
                            s.stackSize++;
                            inventory.setInventorySlotContents(stackIdx, s);
                            hasInsertedInChest = true;
                        }
                    } else {
                        inventory.setInventorySlotContents(stackIdx, stack.stack);
                        hasInsertedInChest = true;
                    }
                }

                if (!inventories.isEmpty()) {
                    int idx = (int)(Math.random() * (double) inventories.size());
                    IInventory inventory = inventories.get(idx);
                    TileEntityPipe[] pipesArray = new TileEntityPipe[pipes.size()];
                    for (int i = 0; i < pipes.size(); i++) pipesArray[i] = pipes.get(i);
                    hasInsertedInChest = Util.insertOnInventory(inventory, stack.stack, inventoriesDirection.get(idx), pipesArray);
                }

                if (!hasInsertedInChest) {
                    if (!pipes.isEmpty()) {
                        int idx = (int) (Math.random() * (double) pipes.size());
                        TileEntityPipe pipe = pipes.get(idx);
                        stack.timer = stackTimer;
                        stack.direction = directions.get(idx).getOpposite();
                        pipe.stacks.add(stack);
                    } else {
                        break;
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

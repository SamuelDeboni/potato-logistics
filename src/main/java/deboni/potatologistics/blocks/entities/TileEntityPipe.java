package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.PipeStack;
import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.Util;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.BlockRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.IItemIO;

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

    public int visualConnections = 0;
    public int visualColor = 0;

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
            worldObj.dropItem(x, y, z, stack.stack);
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
        int meta = worldObj.getBlockMetadata(x, y, z);
        return (meta & (1 << 2)) != 0;
    }

    public Direction getDirection() {
        int meta = worldObj.getBlockMetadata(x, y, z);
        return Direction.getDirectionById(meta >> 3);
    }
    public boolean isPointingTo(int x, int y, int z) {
        int meta = worldObj.getBlockMetadata(this.x, this.y, this.z);
        Direction dir = Direction.getDirectionById(meta >> 3);
        boolean sameX = dir.getOffsetX() == this.x - x;
        boolean sameY = dir.getOffsetY() == this.y - y;
        boolean sameZ = dir.getOffsetZ() == this.z - z;
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

        visualConnections = nbttagcompound.getInteger("visualConnections");
        visualColor = nbttagcompound.getInteger("visualColor");
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for (PipeStack stack : this.stacks) {
            if (stack == null) continue;
            CompoundTag nbttagcompound1 = new CompoundTag();
            stack.writeToNBT(nbttagcompound1);
            nbttaglist.addTag(nbttagcompound1);
        }
        nbttagcompound.put("Items", nbttaglist);
        nbttagcompound.putInt("visualConnections", visualConnections);
        nbttagcompound.putInt("visualColor", visualColor);
    }

    public void calcVisualConnections() {
        int meta = worldObj.getBlockMetadata(x, y, z);
        int type = meta & 0x03;
        Direction pipeDirection = Direction.getDirectionById(meta >> 3).getOpposite();

        boolean isDirectional = (meta & (1 << 2)) != 0;
        visualConnections = 0;
        visualColor = 0;
        for (int i = 0; i < offsets.length; i++) {
            TileEntity te = Util.getBlockTileEntity(worldObj, x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            if (te == null) continue;

            int blockBreakerDirId = 0;
            Block blockN = worldObj.getBlock(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            int blockNMeta = worldObj.getBlockMetadata(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            if (blockN instanceof BlockRotatable) {
                blockBreakerDirId = BlockRotatable.getOrientation(blockNMeta);
            }

            int nid = worldObj.getBlockId(x + offsets[i][0], y + offsets[i][1], z +offsets[i][2]);
            if(te instanceof TileEntityPipe
                    || type != 0 && te instanceof IInventory
                    || Direction.getDirectionById(i) == Direction.UP && nid == PotatoLogisticsMod.blockAutoBasket.id
                    || nid == PotatoLogisticsMod.blockBlockCrusher.id && blockBreakerDirId == i
                    || nid == PotatoLogisticsMod.blockBlockPlacer.id && blockBreakerDirId == i
            ) {
                if (te instanceof TileEntityPipe) {
                    TileEntityPipe pipe = (TileEntityPipe) te;
                    if (isDirectional && pipe.isDirectional()) {
                        if (pipeDirection.getId() == i) {
                            visualConnections |= 1 << i;
                        }
                        if (pipe.isPointingTo(this.x, this.y, this.z)) {
                            visualConnections |= 1 << i;
                        }
                    } else {
                        visualConnections |= 1 << i;
                    }
                } else {
                    visualConnections |= 1 << i;
                    visualColor |= 1 << i;
                }
            }
        }
    }

    int sleepTimer = 0;

    @Override
    public void tick() {
        if (sleepTimer > 0) {
            sleepTimer--;
            return;
        }

        super.tick();

        //calcVisualConnections();

        //if (!stacks.isEmpty()) worldObj.markBlockNeedsUpdate(this.x, this.y, this.z);

        int meta = worldObj.getBlockMetadata(this.x, this.y, this.z);

        int type = meta & 0x03;
        boolean isDirectional = (meta & (1 << 2)) != 0;
        Direction pipeDirection = Direction.getDirectionById(meta >> 3).getOpposite();

        if (type == 1 && stacks.size() < this.stackLimit && timer <= 0) {
            for (int i = 0; i < offsets.length; i++) {
                int x2 = x + offsets[i][0];
                int y2 = y + offsets[i][1];
                int z2 = z + offsets[i][2];

                PipeStack inventoryStack = Util.getItemFromInventory(worldObj, x2, y2, z2, Direction.getDirectionById(i), this.stackTimer);
                if (inventoryStack != null) {
                    stacks.add(inventoryStack);
                    break;
                }
            }

            timer = timerLen;
        }

        timer--;

        if (timer <= 0) {
            calcVisualConnections();
        }

        for (int stackIndex = 0; stackIndex < stacks.size(); stackIndex++) {
            PipeStack stack = stacks.get(stackIndex);
            if (stack == null || stack.stack == null) {
                stacks.remove(stackIndex);
                stackIndex--;
                continue;
            }

            if (stack.timer <= 0) {
                List<TileEntityPipe> pipes = new ArrayList<>(6);
                List<IInventory> inventories = new ArrayList<>(6);

                List<IInventory> ioInventories = new ArrayList<>(6);
                List<IItemIO> itemIOs = new ArrayList<>(6);
                List<sunsetsatellite.catalyst.core.util.Direction> ioDirections = new ArrayList<>(6);

                //List<IInventory> furnaces = new ArrayList<>(6);
                List<Direction> inventoriesDirection = new ArrayList<>(6);
                List<Direction> directions = new ArrayList<>(6);

                for (int i = 0; i < offsets.length; i++) {
                    int x2 = x + offsets[i][0];
                    int y2 = y + offsets[i][1];
                    int z2 = z + offsets[i][2];
                    int nid = worldObj.getBlockId(x2, y2, z2);

                    TileEntity te = Util.getBlockTileEntity(worldObj, x2, y2, z2);


                    if (te instanceof IInventory) {

                        IInventory inventory = (IInventory) te;
                        String inventoryName = inventory.getInvName();
                        boolean isFromIronChests = Objects.equals(inventoryName, "Iron Chest")
                                || Objects.equals(inventoryName, "Gold Chest")
                                || Objects.equals(inventoryName, "Diamond Chest")
                                || Objects.equals(inventoryName, "Steel Chest")
                                || Objects.equals(inventoryName, "Big Chest");

                        if (te instanceof IItemIO && !isFromIronChests) {
                            sunsetsatellite.catalyst.core.util.Direction sdir = sunsetsatellite.catalyst.core.util.Direction.getDirectionFromSide(i).getOpposite();
                            IItemIO itemIo = (IItemIO) te;

                            Connection con = itemIo.getItemIOForSide(sdir);
                            if (type == 2 && (con == Connection.INPUT || con == Connection.BOTH)) {
                                ioInventories.add(inventory);
                                itemIOs.add(itemIo);
                                ioDirections.add(sdir);
                            }
                        } else {

                            if (inventory instanceof TileEntityChest) {
                                inventory = BlockChest.getInventory(worldObj, x2, y2 ,z2);
                            }

                            if (type == 2) {
                                inventories.add(inventory);
                                inventoriesDirection.add(Direction.getDirectionById(i));
                            }
                        }
                    }else if (te instanceof TileEntityPipe && stack.direction != Direction.getDirectionById(i)) {
                        TileEntityPipe pipe = (TileEntityPipe) te;
                        if (pipe.stacks.size() < pipe.stackLimit && (!isDirectional || pipeDirection.getId() == i)) {
                            pipes.add(pipe);
                            directions.add(Direction.getDirectionById(i));
                        }
                    }
                }

                boolean hasInsertedInChest = false;
                if (!ioInventories.isEmpty()) {
                    int idx = (int)(Math.random() * (double) ioInventories.size());
                    IInventory inventory = ioInventories.get(idx);
                    sunsetsatellite.catalyst.core.util.Direction dir = ioDirections.get(idx);
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
                stackIndex--;
            } else {
                stack.timer--;
            }
        }

        if (stacks.isEmpty() && timer == 0) {
            sleepTimer = 20;
        }
    }
    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }
}

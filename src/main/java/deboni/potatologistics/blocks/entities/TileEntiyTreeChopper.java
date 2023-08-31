package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.Util;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import sunsetsatellite.energyapi.impl.TileEntityEnergyConductor;
import sunsetsatellite.energyapi.impl.TileEntityEnergySink;
import sunsetsatellite.sunsetutils.util.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntiyTreeChopper extends TileEntityEnergyConductor {
    public List<ItemStack> stacks = new ArrayList<>();
    public List<int[]> blocksToBreak = new ArrayList<>();

    public TileEntiyTreeChopper() {
        this.setCapacity(3000);
        this.setEnergy(0);
        this.setTransfer(250);

        sunsetsatellite.sunsetutils.util.Direction[] directions = sunsetsatellite.sunsetutils.util.Direction.values();
        for (sunsetsatellite.sunsetutils.util.Direction dir : directions) {
            this.setConnection(dir, Connection.INPUT);
        }
    }

    public void dropItems() {
        for (ItemStack stack : stacks) {
            worldObj.dropItem(xCoord, yCoord, zCoord, stack);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("Items");
        this.stacks = new ArrayList<>();
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            CompoundTag nbttagcompound1 = (CompoundTag) nbttaglist.tagAt(i);
            this.stacks.add(ItemStack.readItemStackFromNbt(nbttagcompound1));
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

    private int fillBlocksToBreak(World world, int x, int y, int z, int depth) {
        int totalEnergyCost = 0;

        if (depth > 1024) return 0;
        int[] coord = new int[]{x, y, z};

        for (int[] c: blocksToBreak) {
            if (Arrays.equals(c, coord)) return 0;
        }

        Block block = world.getBlock(x, y, z);
        if (block == null) return 0;

        if (block instanceof BlockLog || block instanceof BlockLeavesBase) {
        }  else {
            return 0;
        }

        blocksToBreak.add(coord);

        int sum = 0;
        sum += fillBlocksToBreak(world, x, y + 1, z, depth + 1);
        sum += fillBlocksToBreak(world, x, y, z + 1, depth + 1);
        sum += fillBlocksToBreak(world, x, y, z - 1, depth + 1);
        sum += fillBlocksToBreak(world, x + 1, y, z, depth + 1);
        sum += fillBlocksToBreak(world, x - 1, y, z, depth + 1);
        sum += fillBlocksToBreak(world, x, y - 1, z, depth + 1);

        return sum > 0 ? 1 : 0;
    }

    public void breakTree() {
        if (!this.stacks.isEmpty()) return;

        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        Direction dir = Direction.getDirectionById(BlockRotatable.getOrientation(meta));

        int tx = xCoord + dir.getOffsetX();
        int ty = yCoord + dir.getOffsetY();
        int tz = zCoord + dir.getOffsetZ();

        if (blocksToBreak.isEmpty()) fillBlocksToBreak(worldObj, tx, ty, tz, 0);
        if (blocksToBreak.isEmpty()) return;

        int[] b = blocksToBreak.get(blocksToBreak.size() - 1);

        Block block = worldObj.getBlock(b[0], b[1], b[2]);

        ItemStack[] breakResult = null;
        int energyRequired = 100;
        if (block instanceof BlockLog) {
            breakResult = new ItemStack[1];
            breakResult[0] = new ItemStack(block.asItem());
        } else if (block instanceof BlockLeavesBase) {
            breakResult = block.getBreakResult(worldObj, EnumDropCause.WORLD, b[0], b[1], b[2], worldObj.getBlockMetadata(b[0], b[1], b[2]), null);
            energyRequired = 10;
        } else {
            blocksToBreak.remove(blocksToBreak.size() - 1);
            return;
        }

        if (energy < energyRequired) return;
        blocksToBreak.remove(blocksToBreak.size() - 1);
        this.modifyEnergy(-energyRequired);

        worldObj.playSoundEffect(2001, b[0], b[1], b[2], block.id);
        boolean removed = worldObj.setBlockWithNotify(b[0], b[1], b[2], 0);

        if (breakResult == null) return;
        this.stacks.addAll(Arrays.asList(breakResult));
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        PotatoLogisticsMod.LOGGER.info("Energy is: " + this.energy + " blocks to break count: " + blocksToBreak.size());

        if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && !worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord)) {
            return;
        }
        breakTree();

        if (stacks.isEmpty()) return;

        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        Direction dir = Direction.getDirectionById(BlockRotatable.getOrientation(meta));

        int ix = xCoord - dir.getOffsetX();
        int iy = yCoord - dir.getOffsetY();
        int iz = zCoord - dir.getOffsetZ();

        TileEntity outTe = worldObj.getBlockTileEntity(ix, iy, iz) ;

        if (outTe instanceof IInventory) {
            IInventory inventory;
            if (outTe instanceof TileEntityChest) {
                inventory = BlockChest.getInventory(worldObj, ix, iy, iz);
            } else {
                inventory = (IInventory) outTe;
            }
            if (inventory != null) {
                while (!stacks.isEmpty()) {
                    ItemStack stack = stacks.get(0);
                    boolean hasInserted = Util.insertOnInventory(inventory, stack, dir, new TileEntityPipe[0]);
                    if (!hasInserted) break;
                    stacks.remove(0);
                }
            }
        } else {
            while (!stacks.isEmpty()) {
                ItemStack stack = stacks.get(0);
                worldObj.dropItem(ix, iy, iz, stack);
                stacks.remove(0);
            }
        }
    }
}


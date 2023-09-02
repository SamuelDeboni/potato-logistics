package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.Util;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.entity.TileEntityDispenser;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.world.World;
import sunsetsatellite.energyapi.impl.TileEntityEnergyConductor;
import sunsetsatellite.sunsetutils.util.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityMiningDrill extends TileEntityEnergyConductor {
    public List<ItemStack> stacks = new ArrayList<>();
    public List<int[]> blocksToBreak = new ArrayList<>();
    public boolean isActive = false;

    public TileEntityMiningDrill() {
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

    private void fillBlocksToBreak() {
        int radius = 10;

        for (int y = 2; y < yCoord; y++) {
            for (int z = zCoord - radius; z <= zCoord + radius; z++) {
                for (int x = xCoord - radius; x <= xCoord + radius; x++) {
                    Block block = worldObj.getBlock(x, y, z);
                    if (block == null || block.hasTag(BlockTags.IS_WATER) || block.hasTag(BlockTags.IS_LAVA) || block.immovable && block.id != Block.obsidian.id || block.id == Block.bedrock.id) {
                        continue;
                    }
                    blocksToBreak.add(new int[]{x, y, z});
                }
            }
        }
    }

    public void mineArea() {
        if (!this.stacks.isEmpty()) return;

        if (blocksToBreak.isEmpty()) fillBlocksToBreak();
        if (blocksToBreak.isEmpty()) return;

        int[] b = blocksToBreak.get(blocksToBreak.size() - 1);

        Block block = worldObj.getBlock(b[0], b[1], b[2]);
        if (block == null) {
            blocksToBreak.remove(blocksToBreak.size() - 1);
            return;
        }

        ItemStack[] breakResult;
        int energyRequired = 100;
        TileEntity blockTe = worldObj.getBlockTileEntity(b[0], b[1], b[2]);
        breakResult = block.getBreakResult(worldObj, EnumDropCause.PROPER_TOOL, b[0], b[1], b[2], worldObj.getBlockMetadata(b[0], b[1], b[2]), blockTe);

        if (energy < energyRequired) return;
        blocksToBreak.remove(blocksToBreak.size() - 1);
        this.modifyEnergy(-energyRequired);

        worldObj.playSoundEffect(2001, b[0], b[1], b[2], block.id);
        worldObj.setBlockWithNotify(b[0], b[1], b[2], 0);

        if (breakResult == null) return;
        this.stacks.addAll(Arrays.asList(breakResult));
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        //PotatoLogisticsMod.LOGGER.info("Energy is: " + this.energy + " blocks to break count: " + blocksToBreak.size());

        if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && !worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord)) {
            isActive = false;
            return;
        }
        isActive = true;
        mineArea();

        if (stacks.isEmpty()) return;

        int ix = xCoord;
        int iy = yCoord + 1;
        int iz = zCoord;

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
                    boolean hasInserted = Util.insertOnInventory(inventory, stack, Direction.UP, new TileEntityPipe[0]);
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

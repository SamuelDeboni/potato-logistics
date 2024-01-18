package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;

public class TileEntityFilter extends TileEntity implements IInventory {
    private ItemStack[] filterContents;

    public TileEntityFilter(int size) {
        filterContents = new ItemStack[size];
    }

    public TileEntityFilter() {
        this(9);
    }

    @Override
    public int getSizeInventory() {
        return filterContents.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.filterContents[i];
    }


    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (this.filterContents[i] != null) {
            if (this.filterContents[i].stackSize <= j) {
                ItemStack itemstack = this.filterContents[i];
                this.filterContents[i] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = this.filterContents[i].splitStack(j);
            if (this.filterContents[i].stackSize <= 0) {
                this.filterContents[i] = null;
            }
            this.onInventoryChanged();
            return itemstack1;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.filterContents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        this.onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return "Filter";
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        ListTag nbttaglist = nbttagcompound.getList("Items");
        this.filterContents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            CompoundTag nbttagcompound1 = (CompoundTag)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xFF;
            if (j >= this.filterContents.length) continue;
            this.filterContents[j] = ItemStack.readItemStackFromNbt(nbttagcompound1);
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < this.filterContents.length; ++i) {
            if (this.filterContents[i] == null) continue;
            CompoundTag nbttagcompound1 = new CompoundTag();
            nbttagcompound1.putByte("Slot", (byte)i);
            this.filterContents[i].writeToNBT(nbttagcompound1);
            nbttaglist.addTag(nbttagcompound1);
        }
        nbttagcompound.put("Items", nbttaglist);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        if (this.worldObj.getBlockTileEntity(this.x, this.y, this.z) != this) {
            return false;
        }
        return entityPlayer.distanceToSqr((double)this.x + 0.5, (double)this.y + 0.5, (double)this.z + 0.5) <= 64.0;
    }

    @Override
    public void sortInventory() {

    }
}

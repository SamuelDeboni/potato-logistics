package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.blocks.BlockFurnaceBurner;
import deboni.potatologistics.Util;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IItemIO;
import sunsetsatellite.catalyst.energy.api.LookupFuelEnergy;

public class TileEntityBurner extends TileEntity implements IInventory, IItemIO {
    public int maxBurnTemperature;
    private ItemStack[] contents;
    public int maxBurnTime = 0;
    public int currentBurnTime = 0;
    public ItemStack currentFuel;
    public boolean isPowered;
    public TileEntityBurner(){
        contents = new ItemStack[1];
        maxBurnTemperature = 1200;
    }

    public TileEntityBurner(int maxBurnTemperature){
        contents = new ItemStack[1];
        this.maxBurnTemperature = maxBurnTemperature;
    }

    public int consumeFuel() {
        if (this.currentBurnTime > 0) {
            --this.currentBurnTime;
        }

        boolean updated = false;

        if (!this.worldObj.isClientSide) {
            isPowered = worldObj.isBlockIndirectlyGettingPowered(x, y, z) || worldObj.isBlockGettingPowered(x, y, z);

            if (this.currentBurnTime <= 0 && !isPowered) {
                this.maxBurnTime = this.currentBurnTime = this.getBurnTimeFromItem(this.contents[0]);
                if (this.currentBurnTime > 0) {
                    currentFuel = this.contents[0];
                    updated = true;
                    if (this.contents[0] != null) {
                        --this.contents[0].stackSize;
                        if (this.contents[0].stackSize == 0) {
                            this.contents[0] = null;
                        }
                    }
                } else {
                    currentFuel = null;
                }
            }

            Block b = worldObj.getBlock(x, y, z);
            if (b instanceof BlockFurnaceBurner) {
                ((BlockFurnaceBurner) b).setOn(worldObj, x, y, z, this.currentBurnTime > 0);
            }
        }

        if (updated) {
            this.onInventoryChanged();
        }

        return this.currentBurnTime > 0 ? this.maxBurnTemperature : 0;
    }

    public int getSizeInventory()
    {
        return contents.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return contents[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(contents[i] != null)
        {
            if(contents[i].stackSize <= j)
            {
                ItemStack itemstack = contents[i];
                contents[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = contents[i].splitStack(j);
            if(contents[i].stackSize == 0)
            {
                contents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        }
        return null;
    }


    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        contents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();

    }

    public void onInventoryChanged() {
        super.onInventoryChanged();
    }

    public String getInvName()
    {
        return "Coal Burner";
    }

    public void readFromNBT(CompoundTag CompoundTag)
    {
        super.readFromNBT(CompoundTag);
        ListTag ListTag = CompoundTag.getList("Items");
        contents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < ListTag.tagCount(); i++)
        {
            CompoundTag CompoundTag1 = (CompoundTag)ListTag.tagAt(i);
            int j = CompoundTag1.getByte("Slot") & 0xff;
            if(j < contents.length)
            {
                contents[j] = ItemStack.readItemStackFromNbt(CompoundTag1);
            }
        }
        this.currentBurnTime = CompoundTag.getInteger("BurnTime");
        this.maxBurnTime = CompoundTag.getInteger("MaxBurnTime");
        this.maxBurnTemperature = CompoundTag.getInteger("MaxBurnTemperature");
        currentFuel = ItemStack.readItemStackFromNbt(CompoundTag.getCompound("CurrentFuel"));
    }


    public void writeToNBT(CompoundTag CompoundTag)
    {
        super.writeToNBT(CompoundTag);
        ListTag ListTag = new ListTag();
        for(int i = 0; i < contents.length; i++)
        {
            if(contents[i] != null)
            {

                CompoundTag CompoundTag1 = new CompoundTag();
                CompoundTag1.putByte("Slot", (byte)i);
                contents[i].writeToNBT(CompoundTag1);
                ListTag.addTag(CompoundTag1);
            }
        }
        CompoundTag fuel = new CompoundTag();
        if(currentFuel != null){
            currentFuel.writeToNBT(fuel);
        }
        CompoundTag.put("Items", ListTag);
        CompoundTag.putCompound("CurrentFuel",fuel);
        CompoundTag.putInt("BurnTime", (short)this.currentBurnTime);
        CompoundTag.putInt("MaxBurnTime", (short)this.maxBurnTime);
        CompoundTag.putInt("MaxBurnTemperature", (short)this.maxBurnTemperature);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer) {
        if(Util.getBlockTileEntity(worldObj, x, y, z) != this) {
            return false;
        }
        return entityplayer.distanceToSqr((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D) <= 64D;
    }

    @Override
    public void sortInventory() {

    }

    private int getBurnTimeFromItem(ItemStack itemStack) {
        return itemStack == null ? 0 : LookupFuelFurnace.instance.getFuelYield(itemStack.getItem().id) / 2;
    }

    private int getEnergyYieldForItem(ItemStack itemStack) {
        return itemStack == null ? 0 : LookupFuelEnergy.fuelEnergy().getEnergyYield(itemStack.getItem().id);
    }

    public int getBurnTimeRemainingScaled(int i) {
        return this.maxBurnTime == 0 ? 0 : this.currentBurnTime * i / this.maxBurnTime;
    }

    public boolean isBurning() {
        return this.currentBurnTime > 0;
    }

    @Override
    public int getActiveItemSlotForSide(Direction direction) {
        return 0;
    }

    @Override
    public int getActiveItemSlotForSide(Direction direction, ItemStack itemStack) {
        return 0;
    }

    @Override
    public Connection getItemIOForSide(Direction direction) {
        return Connection.BOTH;
    }
    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }
}

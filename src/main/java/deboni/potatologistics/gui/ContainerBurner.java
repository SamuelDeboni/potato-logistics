package deboni.potatologistics.gui;

import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ICrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.energyapi.template.containers.ContainerEnergy;

import java.util.List;

public class ContainerBurner extends ContainerEnergy {

    private int maxBurnTime = 0;
    private int currentBurnTime = 0;
    public ItemStack currentFuel;

    public ContainerBurner(IInventory iInventory, TileEntityBurner tileEntity){
        tile = tileEntity;

        addSlot(new Slot(tileEntity,0, 80,17 + 2 * 18));

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }
        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void updateInventory() {
        // Updates container inventory
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);
            if (ItemStack.areItemStacksEqual(itemstack1, itemstack)) continue;
            itemstack1 = itemstack != null ? itemstack.copy() : null;
            this.inventoryItemStacks.set(i, itemstack1);
            for (ICrafting crafter : this.crafters) {
                crafter.updateInventorySlot(this, i, itemstack1);
            }
        }

        // Updates progress bars
        TileEntityBurner teBurner = (TileEntityBurner) tile;
        for (ICrafting crafter : this.crafters) {
            if (this.currentBurnTime != teBurner.currentBurnTime) {
                crafter.updateCraftingInventoryInfo(this, 0, teBurner.currentBurnTime);
            }
            if (this.maxBurnTime != teBurner.maxBurnTime) {
                crafter.updateCraftingInventoryInfo(this, 1, teBurner.maxBurnTime);
            }
        }

        this.currentBurnTime = ((TileEntityBurner)tile).currentBurnTime;
        this.maxBurnTime = ((TileEntityBurner)tile).maxBurnTime;
    }

    @Override
    public void updateClientProgressBar(int id, int value) {
        TileEntityBurner teBurner = (TileEntityBurner) tile;
        if (id == 0) {
            teBurner.currentBurnTime = value;
        }
        if (id == 1) {
            teBurner.maxBurnTime = value;
        }
    }
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id == 0){ // Quick stack inside the container
            return getSlots(0,1,false);
        }
        return getSlots(1,36, true); // Quick stack inside the inventory

    }

    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id == 0){ // Burner slot -> inventory
            List<Integer> listOut = getSlots(28,9, false); // Hotbar first
            listOut.addAll(getSlots(1,27, false)); // Then Inventory
            return listOut;
            //return getSlots(1,36, true);
        }
        if (target == 2){ // Inventory Fuel Item -> Burner Slot
            return getSlots(0,1,false);
        }
        if (slot.id < 28) { // Main inventory -> Hotbar
            return getSlots(28, 9, false);
        }
        return getSlots(1, 27, false); // Hotbar -> Main Inventory
    }


    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer1) {
        return true;
    }
}
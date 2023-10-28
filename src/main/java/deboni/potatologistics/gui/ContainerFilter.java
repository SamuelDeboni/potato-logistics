package deboni.potatologistics.gui;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class ContainerFilter extends Container {
    private final IInventory inventory;
    private final int numberOfRows;

    public ContainerFilter(InventoryPlayer playerInventory, IInventory inventory) {
        this.inventory = inventory;
        numberOfRows = 1;

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 18));
        }

        int i = (this.numberOfRows - 4) * 18;
        for (int k = 0; k < 3; ++k) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
            }
        }
        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 161 + i));
        }
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int i, EntityPlayer entityPlayer) {
        if (slot.id < 9){
            return getSlots(0, 9, false);
        }
        if (slot.id < 36){
            return getSlots(9, 27, false);
        }
        return getSlots(36, 9, false);
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        int filterSize = this.numberOfRows * 9;
        if (slot.id < filterSize) { // Filter -> Inventory
            List<Integer> listOut = getSlots(filterSize + 27,9, false); // Hotbar first
            listOut.addAll(getSlots(filterSize,27, false)); // Then Inventory
            return listOut;
        }
        return this.getSlots(0, filterSize, false); // Inventory -> Filter
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return this.inventory.canInteractWith(entityPlayer);
    }
}

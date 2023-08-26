package deboni.potatologistics.gui;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class ContainerFilter extends Container {
    private final IInventory inventory;
    private final int numberOfRows;

    public ContainerFilter(IInventory inventory, IInventory playerInventory) {
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
        /*
        int chestSize = this.numberOfRows * 9;
        if (slot.id >= 0 && slot.id < chestSize) {
            return this.getSlots(0, chestSize, false);
        }
        if (action == InventoryAction.MOVE_ALL) {
            if (slot.id >= chestSize && slot.id < chestSize + 27) {
                return this.getSlots(chestSize, 27, false);
            }
            if (slot.id >= chestSize + 27 && slot.id < chestSize + 36) {
                return this.getSlots(chestSize + 27, 9, false);
            }
        } else if (slot.id >= chestSize && slot.id < chestSize + 36) {
            return this.getSlots(chestSize, 36, false);
        }
         */
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        int chestSize = this.numberOfRows * 9;
        if (slot.id < chestSize) {
            return this.getSlots(chestSize, 9, true);
        }
        return this.getSlots(0, chestSize, false);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return this.inventory.canInteractWith(entityPlayer);
    }
}

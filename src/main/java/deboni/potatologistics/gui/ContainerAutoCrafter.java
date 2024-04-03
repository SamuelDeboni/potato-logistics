package deboni.potatologistics.gui;

import deboni.potatologistics.blocks.entities.TileEntityAutoCrafter;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;

import java.util.List;

public class ContainerAutoCrafter extends Container {
    public static class SlotAutoCrafterOut extends SlotCrafting {
        public SlotAutoCrafterOut(EntityPlayer entityplayer, IInventory iinventory, IInventory iinventory1, int i, int j, int k) {
            super(entityplayer, iinventory, iinventory1, i, j, k);
        }

        @Override
        public boolean canPutStackInSlot(ItemStack itemstack) {
            return false;
        }

        public void actualOnPickupFromSlot(ItemStack itemStack) {
            super.onPickupFromSlot(itemStack);
        }

        @Override
        public void onPickupFromSlot(ItemStack itemStack) {
        }
    }

    public static class SlotNoInteract extends Slot {

        public SlotNoInteract(IInventory inventory, int id, int x, int y) {
            super(inventory, id, x, y);
        }

        @Override
        public boolean canPutStackInSlot(ItemStack itemstack) {
            return false;
        }
    }

    public ContainerAutoCrafter(InventoryPlayer inventoryplayer, TileEntityAutoCrafter tile) {
        if (inventoryplayer == null) {
            return;
        }

        SlotAutoCrafterOut slotCrafting = new SlotAutoCrafterOut(inventoryplayer.player, tile.craftMatrix, tile.craftResult, 0, 124, 35);
        this.addSlot(slotCrafting);

        this.addSlot(new SlotNoInteract(tile.extraOutputs, 0, 151, 35));

        for (int yi = 0; yi < 3; ++yi) {
            for (int xi = 0; xi < 3; ++xi) {
                this.addSlot(new Slot(tile.pattern, xi + yi * 3, 30 + xi * 18, 17 + yi * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new SlotNoInteract(tile.craftMatrix, i, 8 + i * 18, 81));
        }

        for (int yi = 0; yi < 3; ++yi) {
            for (int xi = 0; xi < 9; ++xi) {
                this.addSlot(new Slot(inventoryplayer, xi + yi * 9 + 9, 8 + xi * 18, 109 + yi * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventoryplayer, i, 8 + i * 18, 167));
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id == 0) {
            return this.getSlots(0, 1, false);
        }
        if (slot.id >= 1 && slot.id < 9) {
            return this.getSlots(1, 9, false);
        }
        if (action == InventoryAction.MOVE_SIMILAR) {
            if (slot.id >= 10 && slot.id <= 45) {
                return this.getSlots(10, 36, false);
            }
        } else {
            if (slot.id >= 10 && slot.id <= 36) {
                return this.getSlots(10, 27, false);
            }
            if (slot.id >= 37 && slot.id <= 45) {
                return this.getSlots(37, 9, false);
            }
        }
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 10 && slot.id <= 45) {
            if (target == 1) {
                return this.getSlots(1, 9, false);
            }
            if (slot.id <= 36) {
                return this.getSlots(37, 9, false);
            }
            return this.getSlots(10, 27, false);
        } else {
            if (slot.id == 0) {
                return this.getSlots(10, 36, true);
            }
            return this.getSlots(10, 36, false);
        }
    }
}

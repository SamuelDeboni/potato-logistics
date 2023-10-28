package deboni.potatologistics.gui;

import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntityBlastFurnace;
import net.minecraft.core.crafting.LookupFuelFurnace;
import net.minecraft.core.crafting.LookupFuelFurnaceBlast;
import net.minecraft.core.crafting.recipe.RecipesBlastFurnace;
import net.minecraft.core.crafting.recipe.RecipesFurnace;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import net.minecraft.core.player.inventory.slot.SlotCreative;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiBurner extends GuiContainer {

    public String name = "Burner";
    public TileEntityBurner tile;
    public GuiBurner(InventoryPlayer inventoryPlayer, TileEntityBurner tile) {
        super(new ContainerBurner(inventoryPlayer, tile));
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/potatologistics/gui/furnace_burner.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        GL11.glEnable(3553);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        j = (this.width - this.xSize) / 2;
        k = (this.height - this.ySize) / 2;
        if (this.tile.isBurning()) {
            int l = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(j + 81, k + 36 + 12 - l, 176, 12 - l, 14, l + 2);
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x, y, renderPartialTicks);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 70, 6, 0xFF404040);
    }
    public void clickInventory(int x, int y, int mouseButton) {
        int slotId = this.getSlotId(x, y);
        if (slotId == -1) {
            return;
        }
        if (slotId == -999) {
            InventoryAction action = InventoryAction.DROP_HELD_STACK;
            if (mouseButton == 1) {
                action = InventoryAction.DROP_HELD_SINGLE;
            }
            this.mc.playerController.doInventoryAction(this.inventorySlots.windowId, action, null, this.mc.thePlayer);
            return;
        }
        if (!this.mc.thePlayer.getGamemode().consumeBlocks && mouseButton == 2) {
            this.mc.playerController.doInventoryAction(this.inventorySlots.windowId, InventoryAction.CREATIVE_GRAB, new int[]{slotId, 64}, this.mc.thePlayer);
            return;
        }
        InventoryAction action = InventoryAction.CLICK_LEFT;
        boolean shiftPressed = Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
        boolean ctrlPressed = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
        boolean altPressed = Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
        boolean spacePressed = Keyboard.isKeyDown(57);
        if (mouseButton == 10) {
            shiftPressed = true;
            mouseButton = 0;
        }
        int target = 0;
        Slot slot = this.inventorySlots.getSlot(slotId);
        ItemStack stackInSlot = slot != null ? slot.getStack() : null;
        Item itemInSlot = stackInSlot != null ? stackInSlot.getItem() : null;
        int clickedItemId = stackInSlot != null ? stackInSlot.getItem().id : 0;
        ItemStack grabbedItem = this.mc.thePlayer.inventory.getHeldItemStack();
        if (mouseButton == 1) {
            action = InventoryAction.CLICK_RIGHT;
        }
        if (slot instanceof SlotCrafting) {
            if (this.mc.gameSettings.swapCraftingButtons.value) {
                if (shiftPressed && ctrlPressed) {
                    action = InventoryAction.MOVE_SIMILAR;
                } else if (shiftPressed) {
                    action = InventoryAction.MOVE_SINGLE_ITEM;
                } else if (ctrlPressed) {
                    action = InventoryAction.MOVE_STACK;
                }
            } else if (shiftPressed && ctrlPressed) {
                action = InventoryAction.MOVE_SIMILAR;
            } else if (shiftPressed) {
                action = InventoryAction.MOVE_STACK;
            } else if (ctrlPressed) {
                action = InventoryAction.MOVE_SINGLE_ITEM;
            }
        } else if (spacePressed) {
            action = InventoryAction.MOVE_ALL;
        } else if (shiftPressed && ctrlPressed) {
            action = InventoryAction.MOVE_SIMILAR;
        } else if (shiftPressed || altPressed) {
            action = InventoryAction.MOVE_STACK;
        } else if (ctrlPressed) {
            action = InventoryAction.MOVE_SINGLE_ITEM;
        }
        if (this.inventorySlots instanceof ContainerBurner) { // This is the only section that actually really matters
            boolean isFuel = LookupFuelFurnace.instance.getFuelYield(clickedItemId) > 0; // If an item is fuel when shift clicked
            if (isFuel) { // It'll send it into the fuel slot
                target = 2; // If it's not it just goes into the inventory
            }
        }
        if (slot != null && itemInSlot != null && itemInSlot instanceof ItemArmor && mouseButton == 1 && shiftPressed) {
            this.mc.playerController.doInventoryAction(this.inventorySlots.windowId, InventoryAction.EQUIP_ARMOR, new int[]{slot.id}, this.mc.thePlayer);
            return;
        }
        if (slot != null && slot.allowItemInteraction() && grabbedItem != null && grabbedItem.getItem().hasInventoryInteraction() && mouseButton == 1) {
            this.mc.playerController.doInventoryAction(this.inventorySlots.windowId, InventoryAction.INTERACT_GRABBED, new int[]{slot.id}, this.mc.thePlayer);
            return;
        }
        if (slot != null && stackInSlot != null && slot.allowItemInteraction() && stackInSlot.getItem().hasInventoryInteraction() && mouseButton == 1) {
            this.mc.playerController.doInventoryAction(this.inventorySlots.windowId, InventoryAction.INTERACT_SLOT, new int[]{slot.id}, this.mc.thePlayer);
            return;
        }
        int[] args = new int[]{slotId, target};
        this.mc.playerController.doInventoryAction(this.inventorySlots.windowId, action, args, this.mc.thePlayer);
    }
    private int getSlotId(int x, int y) {
        Slot slot = this.getSlotAtPosition(x, y);
        int x2 = (this.width - this.xSize) / 2;
        int y2 = (this.height - this.ySize) / 2;
        boolean flag = x < x2 || y < y2 || x >= x2 + this.xSize || y >= y2 + this.ySize;
        int slotId = -1;
        if (slot != null) {
            slotId = slot.id;
        }
        if (flag) {
            slotId = -999;
        }
        return slotId;
    }


    public void initGui()
    {
        super.initGui();
    }
}
package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

public class BurnerTooltip implements IBTWailaCustomBlockTooltip {
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(PotatoLogisticsMod.MOD_ID, TileEntityBurner.class, this);
        tooltipGroup.addTooltip(TileEntityBurner.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityBurner burner = (TileEntityBurner)tileEntity;
        ItemStack fuel = burner.getStackInSlot(0);
        guiBlockOverlay.drawStringWithShadow("Burn time: " + burner.currentBurnTime + "t", 0);
        ItemStack[] stacks = new ItemStack[]{fuel};
        guiBlockOverlay.drawItemList(stacks, 0);
    }
}

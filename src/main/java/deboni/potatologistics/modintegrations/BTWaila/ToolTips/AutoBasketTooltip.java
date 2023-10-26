package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityAutoBasket;
import net.minecraft.core.block.entity.TileEntity;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

public class AutoBasketTooltip implements IBTWailaCustomBlockTooltip {
    public AutoBasketTooltip() {
    }

    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(PotatoLogisticsMod.MOD_ID, TileEntityAutoBasket.class, this);
        tooltipGroup.addTooltip(TileEntityAutoBasket.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityAutoBasket basket = (TileEntityAutoBasket)tileEntity;
        int max = basket.getMaxUnits();
        int current = basket.getNumUnitsInside();
        guiBlockOverlay.drawStringWithShadow("Stored items: " + current + "/" + max, 0);
    }
}

package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.blocks.entities.TileEntityAutoBasket;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class AutoBasketTooltip extends TileTooltip<TileEntityAutoBasket> {
    @Override
    public void initTooltip() {
        addClass(TileEntityAutoBasket.class);
    }
    @Override
    public void drawAdvancedTooltip(TileEntityAutoBasket basket, AdvancedInfoComponent advancedInfoComponent) {
        int max = basket.getMaxUnits();
        int current = basket.getNumUnitsInside();
        advancedInfoComponent.drawStringWithShadow("Stored items: " + current + "/" + max, 0);
    }
}

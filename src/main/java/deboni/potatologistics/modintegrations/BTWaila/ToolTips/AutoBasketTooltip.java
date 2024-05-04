package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.blocks.entities.TileEntityChute;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class AutoBasketTooltip extends TileTooltip<TileEntityChute> {
    @Override
    public void initTooltip() {
        addClass(TileEntityChute.class);
    }
    @Override
    public void drawAdvancedTooltip(TileEntityChute basket, AdvancedInfoComponent advancedInfoComponent) {
        int max = basket.getMaxUnits();
        int current = basket.getNumUnitsInside();
        advancedInfoComponent.drawStringWithShadow("Stored items: " + current + "/" + max, 0);
    }
}

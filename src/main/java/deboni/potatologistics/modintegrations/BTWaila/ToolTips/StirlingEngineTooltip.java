package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.blocks.entities.TileEntityStirlingEngine;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class StirlingEngineTooltip extends TileTooltip<TileEntityStirlingEngine> {
    @Override
    public void initTooltip() {
        addClass(TileEntityStirlingEngine.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityStirlingEngine engine, AdvancedInfoComponent advancedInfoComponent) {
        advancedInfoComponent.drawStringWithShadow("Temperature: " + engine.temperature + "°C", 0);
    }
}

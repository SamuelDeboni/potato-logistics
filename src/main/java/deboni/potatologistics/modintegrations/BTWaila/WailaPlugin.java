package deboni.potatologistics.modintegrations.BTWaila;

import deboni.potatologistics.modintegrations.BTWaila.ToolTips.*;
import org.slf4j.Logger;
import toufoumaster.btwaila.BTWailaCustomTootltipPlugin;

public class WailaPlugin implements BTWailaCustomTootltipPlugin {
    @Override
    public void initializePlugin(Logger logger) {
        new AutoBasketTooltip().addTooltip();
        new FilterTooltip().addTooltip();
        new BurnerTooltip().addTooltip();
        new StirlingEngineTooltip().addTooltip();
        new EntityEnergyTooltip().addTooltip();
    }
}

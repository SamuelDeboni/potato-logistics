package deboni.potatologistics.modintegrations.BTWaila;

import deboni.potatologistics.blocks.entities.TileEntityAutoCrafter;
import deboni.potatologistics.blocks.entities.TileEntityFilter;
import deboni.potatologistics.modintegrations.BTWaila.ToolTips.AutoBasketTooltip;
import deboni.potatologistics.modintegrations.BTWaila.ToolTips.BurnerTooltip;
import deboni.potatologistics.modintegrations.BTWaila.ToolTips.EntityEnergyTooltip;
import deboni.potatologistics.modintegrations.BTWaila.ToolTips.StirlingEngineTooltip;
import org.slf4j.Logger;
import toufoumaster.btwaila.entryplugins.waila.BTWailaCustomTooltipPlugin;
import toufoumaster.btwaila.entryplugins.waila.BTWailaPlugin;
import toufoumaster.btwaila.tooltips.TooltipRegistry;

public class WailaPlugin implements BTWailaCustomTooltipPlugin {
    public static AutoBasketTooltip AUTO_BASKET = new AutoBasketTooltip();
    public static BurnerTooltip BURNER = new BurnerTooltip();
    public static EntityEnergyTooltip ENERGY_ENTITY = new EntityEnergyTooltip();
    public static StirlingEngineTooltip STIRLING_ENGINE = new StirlingEngineTooltip();
    @Override
    public void initializePlugin(TooltipRegistry tooltipRegistry, Logger logger) {
        BTWailaPlugin.INVENTORY.addClass(TileEntityFilter.class);
        BTWailaPlugin.INVENTORY.addClass(TileEntityAutoCrafter.class);
        tooltipRegistry.register(AUTO_BASKET);
        tooltipRegistry.register(BURNER);
        tooltipRegistry.register(ENERGY_ENTITY);
        tooltipRegistry.register(STIRLING_ENGINE);
    }
}

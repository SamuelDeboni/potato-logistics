package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityStirlingEngine;
import net.minecraft.core.block.entity.TileEntity;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

public class StirlingEngineTooltip implements IBTWailaCustomBlockTooltip {
    @Override
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(PotatoLogisticsMod.MOD_ID, TileEntityStirlingEngine.class, this);
        tooltipGroup.addTooltip(TileEntityStirlingEngine.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    @Override
    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityStirlingEngine engine = (TileEntityStirlingEngine)tileEntity;
        guiBlockOverlay.drawStringWithShadow("Temperature: " + engine.temperature + "°C", 0);
    }
}

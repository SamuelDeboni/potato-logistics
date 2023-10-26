package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityFilter;
import net.minecraft.core.player.inventory.IInventory;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.tooltips.block.InventoryTooltip;

public class FilterTooltip extends InventoryTooltip {
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(PotatoLogisticsMod.MOD_ID, IInventory.class, this);
        tooltipGroup.addTooltip(TileEntityFilter.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }
}

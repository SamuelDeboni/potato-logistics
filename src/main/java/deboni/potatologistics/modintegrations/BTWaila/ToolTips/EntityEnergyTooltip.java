package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.*;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergy;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

public class EntityEnergyTooltip implements IBTWailaCustomBlockTooltip {
    @Override
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(PotatoLogisticsMod.MOD_ID, TileEntityEnergy.class, this);
        tooltipGroup.addTooltip(TileEntityCoil.class);
        tooltipGroup.addTooltip(TileEntityEnergyConnector.class);
        tooltipGroup.addTooltip(TileEntityMiningDrill.class);
        tooltipGroup.addTooltip(TileEntityTreeChopper.class);
        tooltipGroup.addTooltip(TileEntityCapacitor.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    @Override
    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityEnergy entityEnergy = (TileEntityEnergy) tileEntity;
        int capacity = entityEnergy.capacity;
        int current = entityEnergy.energy;
        guiBlockOverlay.drawStringWithShadow("Stored energy: " + current + "/" + capacity, 0);

    }
}

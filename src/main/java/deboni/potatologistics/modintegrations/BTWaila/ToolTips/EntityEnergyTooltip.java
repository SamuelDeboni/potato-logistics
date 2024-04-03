package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.blocks.entities.*;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergy;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

public class EntityEnergyTooltip extends TileTooltip<TileEntityEnergy> {
    @Override
    public void initTooltip() {
        addClass(TileEntityCapacitor.class);
        addClass(TileEntityCoil.class);
        addClass(TileEntityEnergyConnector.class);
        addClass(TileEntityMiningDrill.class);
        addClass(TileEntityTreeChopper.class);
        addClass(TileEntityHeater.class);
    }
    @Override
    public void drawAdvancedTooltip(TileEntityEnergy entityEnergy, AdvancedInfoComponent advancedInfoComponent) {
        int capacity = entityEnergy.capacity;
        int current = entityEnergy.energy;
        advancedInfoComponent.drawStringWithShadow("Stored energy: " + current + "/" + capacity, 0);
    }
}

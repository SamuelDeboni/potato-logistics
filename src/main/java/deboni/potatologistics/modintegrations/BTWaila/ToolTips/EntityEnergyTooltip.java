package deboni.potatologistics.modintegrations.BTWaila.ToolTips;

import deboni.potatologistics.blocks.entities.TileEntityCapacitor;
import deboni.potatologistics.blocks.entities.TileEntityCoil;
import deboni.potatologistics.blocks.entities.TileEntityEnergyConnector;
import deboni.potatologistics.blocks.entities.TileEntityMiningDrill;
import deboni.potatologistics.blocks.entities.TileEntityTreeChopper;
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
    }
    @Override
    public void drawAdvancedTooltip(TileEntityEnergy entityEnergy, AdvancedInfoComponent advancedInfoComponent) {
        int capacity = entityEnergy.capacity;
        int current = entityEnergy.energy;
        advancedInfoComponent.drawStringWithShadow("Stored energy: " + current + "/" + capacity, 0);
    }
}

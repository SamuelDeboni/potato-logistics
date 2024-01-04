package deboni.potatologistics.blocks.entities;

import net.minecraft.core.block.BlockRedstone;
import net.minecraft.core.block.BlockRedstoneWire;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

public class TileEntityCapacitor extends TileEntityEnergyConductor {

    public int prevEnergyLevel = 0;

    public TileEntityCapacitor(int capacity) {
        this.setCapacity(capacity);
        setEnergy(0);
        setTransfer(32);

        for (Direction dir: Direction.values()) {
            setConnection(dir, sunsetsatellite.catalyst.core.util.Connection.OUTPUT);
        }
        setConnection(Direction.Y_POS, sunsetsatellite.catalyst.core.util.Connection.INPUT);
    }

    public boolean needPower() {
        int energyDelta = energy - prevEnergyLevel;
        float energyPercent = (float)energy / (float)capacity;

        return energyPercent < 0.8 || energyDelta < 0;
    }

    @Override
    public void tick() {
        prevEnergyLevel = energy;
        super.tick();
    }
}

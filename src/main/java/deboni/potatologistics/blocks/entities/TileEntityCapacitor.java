package deboni.potatologistics.blocks.entities;

import net.minecraft.core.block.BlockRedstone;
import net.minecraft.core.block.BlockRedstoneWire;
import sunsetsatellite.sunsetutils.util.Direction;

public class TileEntityCapacitor extends TileEntityEnergyConnector {

    public int prevEnergyLevel = 0;

    public TileEntityCapacitor(int capacity) {
        setCapacity(capacity);
        setEnergy(0);
        setTransfer(32);

        for (Direction dir: Direction.values()) {
            setConnection(dir, sunsetsatellite.sunsetutils.util.Connection.OUTPUT);
        }
        setConnection(Direction.Y_POS, sunsetsatellite.sunsetutils.util.Connection.INPUT);
    }

    public boolean needPower() {
        int energyDelta = energy - prevEnergyLevel;
        float energyPercent = (float)energy / (float)capacity;

        return energyPercent < 0.8 || energyDelta < 0;
    }

    @Override
    public void updateEntity() {
        prevEnergyLevel = energy;
        super.updateEntity();
    }
}

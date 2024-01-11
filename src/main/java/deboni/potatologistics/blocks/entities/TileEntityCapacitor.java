package deboni.potatologistics.blocks.entities;

import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRedstone;
import net.minecraft.core.block.BlockRedstoneWire;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

public class TileEntityCapacitor extends TileEntityEnergyConductor {

    public int prevEnergyLevel = 0;

    public TileEntityCapacitor(int capacity) {
        setCapacity(capacity);
        setEnergy(0);
        setTransfer(32);

        PotatoLogisticsMod.LOGGER.info("Capacity: " + capacity);

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
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }


    @Override
    public void tick() {
        super.tick();

        prevEnergyLevel = energy;
        worldObj.markBlockNeedsUpdate(x, y, z);
    }
}

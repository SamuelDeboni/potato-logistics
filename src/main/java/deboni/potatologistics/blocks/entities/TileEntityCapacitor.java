package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

public class TileEntityCapacitor extends TileEntityEnergyConductor {

    public TileEntityCapacitor() {
        setCapacity(100000);
        setEnergy(0);
        setTransfer(32);

        PotatoLogisticsMod.LOGGER.info("Capacity: " + capacity);

        for (Direction dir: Direction.values()) {
            setConnection(dir, sunsetsatellite.catalyst.core.util.Connection.OUTPUT);
        }
        setConnection(Direction.Y_POS, sunsetsatellite.catalyst.core.util.Connection.INPUT);
    }

    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        CompoundTag.putBoolean("needPower", needPower);
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        needPower = CompoundTag.getBoolean("needPower");
    }

    public boolean needPower = true;

    @Override
    public void tick() {
        super.tick();

        worldObj.notifyBlocksOfNeighborChange(x, y, z, PotatoLogisticsMod.blockCapacitorLv.id);
        worldObj.markBlockDirty(x, y, z);

        float energyPercent = (float)energy / (float)capacity;
        if (energyPercent > 0.8 && needPower) {
            needPower = false;
        }

        if (energyPercent < 0.2 && !needPower) {
            needPower = true;
        }
    }
}

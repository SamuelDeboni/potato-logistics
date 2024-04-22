package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.BlockCapacitor;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

public class TileEntityCapacitor extends TileEntityEnergyConductor {

    public TileEntityCapacitor() {
        setCapacity(128000);
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
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
    }


    @Override
    public void tick() {
        super.tick();

        if (!worldObj.isClientSide) {
            boolean needPower = worldObj.getBlockMetadata(x, y, z) == 0;
            worldObj.markBlocksDirty(x, y, z, x, y, z);
            worldObj.notifyBlocksOfNeighborChange(x, y, z, needPower ? 15 : 0);

            float energyPercent = (float) energy / (float) capacity;
            if (energyPercent > 0.8 && needPower) {
                needPower = false;
                worldObj.markBlocksDirty(x, y, z, x, y, z);
                worldObj.setBlockMetadataWithNotify(x, y, z, 1);
                BlockCapacitor.notifyNeighbors(worldObj, x, y, z, PotatoLogisticsMod.blockCapacitorLv.id);
            }

            if (energyPercent < 0.2 && !needPower) {
                worldObj.markBlocksDirty(x, y, z, x, y, z);
                worldObj.setBlockMetadataWithNotify(x, y, z, 0);
                BlockCapacitor.notifyNeighbors(worldObj, x, y, z, PotatoLogisticsMod.blockCapacitorLv.id);
            }
        }
    }
}

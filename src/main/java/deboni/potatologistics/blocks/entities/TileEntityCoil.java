package deboni.potatologistics.blocks.entities;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.energyapi.impl.TileEntityEnergyConductor;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;

public class TileEntityCoil extends TileEntityEnergyConductor {

    public TileEntityCoil() {
        setCapacity(10000);
        setEnergy(0);
        setTransfer(32);

        for (Direction dir: Direction.values()) {
            setConnection(dir, Connection.OUTPUT);
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord - 1, zCoord);
        if (te instanceof TileEntityStirlingEngine) {
            TileEntityStirlingEngine engine = (TileEntityStirlingEngine) te;
            int energyProduced = engine.consumeEnergy();
            modifyEnergy(energyProduced);
        }
    }
    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }


}

package deboni.potatologistics.blocks.entities;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.Util;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

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
    public void tick() {
        super.tick();
        TileEntity te = Util.getBlockTileEntity(worldObj, x, y - 1, z);
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

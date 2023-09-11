package deboni.potatologistics.blocks.entities;

import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.entity.TileEntity;
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
            PotatoLogisticsMod.LOGGER.info("Energy = " + energyProduced);
            modifyEnergy(energyProduced);
        }
    }


}

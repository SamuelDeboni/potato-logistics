package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.energyapi.template.tiles.TileEntityGenerator;

public class TileEntityStirlingEngine extends TileEntity {
    public int temperature;
    int maxTemperature = 1000;
    int minTemperature = 200;
    int maxEnergy = 16;

    public int consumeEnergy() {
        if (temperature > minTemperature) {
            return (maxEnergy * (temperature - minTemperature)) / (maxTemperature - minTemperature) + 1;
        }
        return 0;
    }

    @Override
    public void updateEntity() {
        TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
        int burnerPower = 0;
        if (te instanceof TileEntityFurnaceBurner) {
            TileEntityFurnaceBurner burner = (TileEntityFurnaceBurner) te;
            burnerPower = burner.consumeFuel();
            temperature = Math.min(temperature + burnerPower, maxTemperature);
            PotatoLogisticsMod.LOGGER.info("temperature = " + temperature);
        }
        if (temperature > 0 && burnerPower == 0) temperature--;
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("temperature", temperature);
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        temperature = nbttagcompound.getInteger("temperature");
    }
}

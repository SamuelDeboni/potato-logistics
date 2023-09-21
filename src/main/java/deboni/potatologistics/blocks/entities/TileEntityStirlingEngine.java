package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.entity.TileEntity;

public class TileEntityStirlingEngine extends TileEntity {
    public int temperature;
    public int targetTemperature;
    public int maxTemperature = 1000;
    public int minTemperature = 200;
    public int maxEnergy = 16;

    public int consumeEnergy() {
        if (temperature > minTemperature) {
            return (maxEnergy * (temperature - minTemperature)) / (maxTemperature - minTemperature);
        }
        return 0;
    }

    @Override
    public void updateEntity() {
        TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
        if (te instanceof TileEntityBurner) {
            TileEntityBurner burner = (TileEntityBurner) te;
            targetTemperature = Math.min(burner.consumeFuel(), maxTemperature);
        } else {
            targetTemperature = 0;
        }

        if (temperature != targetTemperature) {
            int delta = (int)Math.signum(targetTemperature - temperature) * 4;
            temperature += delta;
            temperature = Math.max(Math.min(temperature, maxTemperature), 0);

            worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
        }

        PotatoLogisticsMod.LOGGER.info("temp = " + temperature);
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

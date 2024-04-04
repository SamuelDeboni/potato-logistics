package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet140TileEntityData;

public class TileEntityStirlingEngine extends TileEntity {
    public int temperature;
    public int targetTemperature;
    public int maxTemperature = 800;
    public int minTemperature = 64;
    public int maxEnergy = 16;
    private final int serverTickRate = 10;
    private int ticksSinceLastPacket = 0;

    public int consumeEnergy() {
        if (temperature > minTemperature) {
            return (maxEnergy * (temperature - minTemperature)) / (maxTemperature - minTemperature);
        }
        return 0;
    }

    @Override
    public void tick() {
        ticksSinceLastPacket++;
        TileEntity te = worldObj.getBlockTileEntity(x, y-1, z);
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

            worldObj.markBlockNeedsUpdate(x, y, z);
            sendPacket();
        }
    }
    public void sendPacket(){
        if (ticksSinceLastPacket >= serverTickRate){
            onInventoryChanged();
            ticksSinceLastPacket = 0;
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("temperature", temperature);
        nbttagcompound.putInt("maxTemperature", maxTemperature);
        nbttagcompound.putInt("maxEnergy", maxEnergy);
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        temperature = nbttagcompound.getInteger("temperature");
        maxTemperature = nbttagcompound.getInteger("maxTemperature");
        maxEnergy = nbttagcompound.getInteger("maxEnergy");
    }
    @Override
    public Packet getDescriptionPacket() {
        return new Packet140TileEntityData(this);
    }
}

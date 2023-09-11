package deboni.potatologistics.blocks.entities;

import net.minecraft.core.block.entity.TileEntity;

public class TileEntityStirlingEngine extends TileEntity {
    public int temperature;

    @Override
    public void updateEntity() {
        TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord-1, zCoord);
        if (te instanceof TileEntityFurnaceBurner) {
            TileEntityFurnaceBurner burner = (TileEntityFurnaceBurner) te;
            if (burner.currentBurnTime > 0 && this.temperature > 1000) {
                temperature += 3;
            }
        }
        if (temperature > 0) temperature--;
    }
}

package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.BlockFurnace;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBlastFurnace;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.entity.TileEntityTrommel;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

public class TileEntityHeater extends TileEntityEnergyConductor {
    public TileEntityHeater() {
        this.setCapacity(3000);
        this.setEnergy(0);
        this.setTransfer(250);

        sunsetsatellite.catalyst.core.util.Direction[] directions = sunsetsatellite.catalyst.core.util.Direction.values();
        for (sunsetsatellite.catalyst.core.util.Direction dir : directions) {
            this.setConnection(dir, Connection.INPUT);
        }
    }

    @Override
    public void tick() {
        super.tick();

        TileEntity tileTop = worldObj.getBlockTileEntity(x, y + 1, z);
        if (tileTop instanceof TileEntityFurnace) {
            int energyConsumption = 8;
            if (tileTop instanceof TileEntityBlastFurnace) {
                energyConsumption *= 2;
            }

            TileEntityFurnace furnace = (TileEntityFurnace) tileTop;
            if (this.energy >= energyConsumption) {
                furnace.maxBurnTime = 10;
                furnace.currentBurnTime = 10;
                furnace.maxCookTime = 100;

                BlockFurnace.updateFurnaceBlockState(true, worldObj, this.x, this.y + 1, this.z);

                modifyEnergy(-energyConsumption);
            } else {
                furnace.maxCookTime = 200;
                BlockFurnace.updateFurnaceBlockState(false, worldObj, this.x, this.y + 1, this.z);
            }
        }

        if (tileTop instanceof TileEntityTrommel) {
            if (this.energy >= 8) {
                TileEntityTrommel trommel = (TileEntityTrommel) tileTop;
                trommel.burnTime = 50;

                modifyEnergy(-8);
            }
        }
    }
}

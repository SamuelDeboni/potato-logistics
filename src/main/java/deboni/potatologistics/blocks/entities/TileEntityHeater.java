package deboni.potatologistics.blocks.entities;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import deboni.potatologistics.Util;
import deboni.potatologistics.PotatoLogisticsMod;
import net.minecraft.core.block.BlockFurnace;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityBlastFurnace;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.entity.TileEntityTrommel;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryBlastFurnace;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.energy.impl.TileEntityEnergyConductor;

import java.util.List;

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

    private static boolean furnaceCanSmelt(TileEntityFurnace furnace, boolean isBlast) {
        if (furnace.getStackInSlot(0) == null) {
            return false;
        }
        List<RecipeEntryFurnace> listF = Registries.RECIPES.getAllFurnaceRecipes();
        List<RecipeEntryBlastFurnace> listB = Registries.RECIPES.getAllBlastFurnaceRecipes();

        ItemStack itemstack = null;
        if (isBlast) {
            for (RecipeEntryBlastFurnace recipeEntryBase : listB) {
                if (recipeEntryBase == null || !recipeEntryBase.matches(furnace.getStackInSlot(0))) continue;
                itemstack = recipeEntryBase.getOutput();
            }
        } else {
            for (RecipeEntryFurnace recipeEntryBase : listF) {
                if (recipeEntryBase == null || !recipeEntryBase.matches(furnace.getStackInSlot(0))) continue;
                itemstack = recipeEntryBase.getOutput();
            }
        }

        if (itemstack == null) {
            return false;
        }
        if (furnace.getStackInSlot(2) == null) {
            return true;
        }
        if (!furnace.getStackInSlot(2).isItemEqual(itemstack)) {
            return false;
        }
        if (furnace.getStackInSlot(2).stackSize < furnace.getInventoryStackLimit() && furnace.getStackInSlot(2).stackSize < furnace.getStackInSlot(2).getMaxStackSize()) {
            return true;
        }
        return furnace.getStackInSlot(2).stackSize < itemstack.getMaxStackSize();
    }


    @Override
    public void tick() {
        super.tick();

        TileEntity tileTop = Util.getBlockTileEntity(worldObj, x, y + 1, z);
        boolean isBlast = tileTop instanceof TileEntityBlastFurnace;

        if (tileTop instanceof TileEntityFurnace) {
            int energyConsumption = isBlast ? 16 : 8;

            TileEntityFurnace furnace = (TileEntityFurnace) tileTop;
            if (this.energy >= energyConsumption && furnaceCanSmelt(furnace, isBlast)) {
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

                if (trommel.currentItemBurnTime > 0) {
                    modifyEnergy(-8);
                }
            }
        }
    }
}

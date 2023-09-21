package deboni.potatologistics.gui;

import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.core.crafting.ICrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.energyapi.template.containers.ContainerEnergy;

import java.util.Iterator;

public class ContainerBurner extends ContainerEnergy {

    private int maxBurnTime = 0;
    private int currentBurnTime = 0;
    public ItemStack currentFuel;

    public ContainerBurner(IInventory iInventory, TileEntityBurner tileEntity){
        tile = tileEntity;

        addSlot(new Slot(tileEntity,0, 80,17 + 2 * 18));

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void updateInventory() {
        //super.updateInventory();
        Iterator var1 = this.crafters.iterator();

        while(var1.hasNext()) {
            Object crafter = var1.next();
            ICrafting icrafting = (ICrafting)crafter;

            /*
            if (this.currentBurnTime != ((TileEntityGenerator)tile).currentBurnTime) {
                icrafting.updateCraftingInventoryInfo(this, 1, ((TileEntityGenerator)tile).currentBurnTime);
            }

            if (this.maxBurnTime != ((TileEntityGenerator)tile).maxBurnTime) {
                icrafting.updateCraftingInventoryInfo(this, 3, ((TileEntityGenerator)tile).maxBurnTime);
            }
             */
        }

        this.currentBurnTime = ((TileEntityBurner)tile).currentBurnTime;
        this.maxBurnTime = ((TileEntityBurner)tile).maxBurnTime;
    }

    public void updateClientProgressBar(int id, int value) {
        /*
        if (id == 1) {
            ((TileEntityGenerator)tile).currentBurnTime = value;
        }

        if (id == 3) {
            ((TileEntityGenerator)tile).maxBurnTime = value;
        }
         */
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer1) {
        return true;
    }
}
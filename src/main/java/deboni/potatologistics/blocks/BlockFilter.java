package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityFilter;
import deboni.potatologistics.gui.ContainerFilter;
import deboni.potatologistics.gui.GuiFilter;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.World;
import sunsetsatellite.energyapi.EnergyAPI;

public class BlockFilter extends BlockTileEntity {
    public BlockFilter(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityFilter();
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        dropFilterContent(world, x, y, z);
        super.onBlockRemoval(world, x, y, z);
    }

    public static void dropFilterContent(World world, int x, int y, int z) {
        TileEntityFilter tileEntityFilter = (TileEntityFilter) world.getBlockTileEntity(x, y, z);
        if (tileEntityFilter == null) {
            System.out.println("Can't drop chest items because tile entity is null at x: " + x + " y:" + y + " z: " + z);
            return;
        }
        for (int i = 0; i < tileEntityFilter.getSizeInventory(); ++i) {
            ItemStack itemStack = tileEntityFilter.getStackInSlot(i);
            if (itemStack == null) continue;
            EntityItem item = world.dropItem(x, y, z, itemStack);
            item.xd *= 0.5;
            item.yd *= 0.5;
            item.zd *= 0.5;
            item.delayBeforeCanPickup = 0;
        }
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.isClientSide) {
            return true;
        }
        IInventory inventory = (IInventory) world.getBlockTileEntity(x, y, z);
        EnergyAPI.displayGui(player, new GuiFilter(inventory, player.inventory), new ContainerFilter(inventory, player.inventory), player.inventory);
        return true;
    }
}

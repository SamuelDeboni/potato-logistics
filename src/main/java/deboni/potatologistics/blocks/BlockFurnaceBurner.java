package deboni.potatologistics.blocks;

import deboni.potatologistics.blocks.entities.TileEntityFurnaceBurner;
import deboni.potatologistics.gui.ContainerBurner;
import deboni.potatologistics.gui.GuiBurner;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import sunsetsatellite.energyapi.EnergyAPI;

public class BlockFurnaceBurner extends BlockTileEntityRotatable {
    public BlockFurnaceBurner(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityFurnaceBurner tile = (TileEntityFurnaceBurner) world.getBlockTileEntity(x, y, z);
            if(tile != null) {
                EnergyAPI.displayGui(player, new GuiBurner(player.inventory, tile), new ContainerBurner(player.inventory, tile), tile);
            }
            return true;
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityFurnaceBurner();
    }
}

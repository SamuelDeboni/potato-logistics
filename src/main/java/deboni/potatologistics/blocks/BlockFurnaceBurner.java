package deboni.potatologistics.blocks;

import com.mojang.nbt.CompoundTag;
import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityBurner;
import deboni.potatologistics.blocks.entities.TileEntityMiningDrill;
import deboni.potatologistics.gui.ContainerBurner;
import deboni.potatologistics.gui.GuiBurner;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFurnace;
import net.minecraft.core.block.BlockPistonBase;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.World;
import sunsetsatellite.energyapi.EnergyAPI;
import sunsetsatellite.energyapi.interfaces.mixins.IEntityPlayer;
import sunsetsatellite.energyapi.template.tiles.TileEntityMachine;

import java.util.Random;

public class BlockFurnaceBurner extends BlockTileEntityRotatable {
    public BlockFurnaceBurner(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.isClientSide) {
            TileEntityBurner tile = (TileEntityBurner) world.getBlockTileEntity(z, y, x);
            if (tile != null) {
                ((IEntityPlayer)player).displayGuiScreen_energyapi(tile);
            }
        }

        return true;
    }

    public void setOn(World world, int x, int y, int z, boolean isOn) {
        int targetId = isOn ? PotatoLogisticsMod.blockFurnaceBurnerOn.id : PotatoLogisticsMod.blockFurnaceBurner.id;
        if (targetId != world.getBlockId(x, y, z)) {
            int meta = world.getBlockMetadata(x, y, z);
            TileEntity te = world.getBlockTileEntity(x, y, z);

            world.setBlockWithNotify(x, y, z, targetId);
            world.setBlockMetadataWithNotify(x, y, z, meta);
            te.validate();
            world.setBlockTileEntity(x, y, z, te);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (blockId > 0 && Block.blocksList[blockId].canProvidePower()) {
            boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockGettingPowered(x, y, z);
            if (flag) {
                world.scheduleBlockUpdate(x, y, z, this.id, 0);
            }
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityBurner(1000);
    }
}

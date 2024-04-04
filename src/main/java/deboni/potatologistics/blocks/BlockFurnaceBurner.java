package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFurnace;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;

import java.util.Random;

public class BlockFurnaceBurner extends BlockTileEntityRotatable {
    protected Random furnaceRand = new Random();
    public BlockFurnaceBurner(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if(!world.isClientSide)
        {
            TileEntityBurner tile = (TileEntityBurner) world.getBlockTileEntity(x, y, z);
            if(tile != null) {
                Catalyst.displayGui(player, tile, tile.getInvName());
            }
        }

        return true;
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        int blockId = world.getBlockId(x,y,z);
        if (blockId == PotatoLogisticsMod.blockFurnaceBurner.id || blockId == PotatoLogisticsMod.blockFurnaceBurnerOn.id) {return;}
        TileEntityBurner tileEntityBurner = (TileEntityBurner)world.getBlockTileEntity(x, y, z);
        for (int l = 0; l < tileEntityBurner.getSizeInventory(); ++l) {
            ItemStack itemstack = tileEntityBurner.getStackInSlot(l);
            if (itemstack == null) continue;
            float f = this.furnaceRand.nextFloat() * 0.8f + 0.1f;
            float f1 = this.furnaceRand.nextFloat() * 0.8f + 0.1f;
            float f2 = this.furnaceRand.nextFloat() * 0.8f + 0.1f;
            while (itemstack.stackSize > 0) {
                int i1 = this.furnaceRand.nextInt(21) + 10;
                if (i1 > itemstack.stackSize) {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)x + f, (float)y + f1, (float)z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
                float f3 = 0.05f;
                entityitem.xd = (float)this.furnaceRand.nextGaussian() * f3;
                entityitem.yd = (float)this.furnaceRand.nextGaussian() * f3 + 0.2f;
                entityitem.zd = (float)this.furnaceRand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            }
        }
        super.onBlockRemoved(world, x, y, z, data);
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
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        return new ItemStack[]{new ItemStack(PotatoLogisticsMod.blockFurnaceBurner)};
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        TileEntityBurner tile = new TileEntityBurner(1200);
        tile.maxBurnTemperature = 800;
        return tile;
    }
}

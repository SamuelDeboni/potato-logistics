package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityAutoCrafter;
import deboni.potatologistics.blocks.entities.TileEntityBurner;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;

import java.util.Random;

public class BlockAutoCrafter extends BlockTileEntityRotatable {

    protected Random crafterRand = new Random();

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isClientSide) {
            int l = world.getBlockId(x, y, z - 1);
            int i1 = world.getBlockId(x, y, z + 1);
            int j1 = world.getBlockId(x - 1, y, z);
            int k1 = world.getBlockId(x + 1, y, z);
            byte byte0 = 3;

            if (Block.solid[i1] && !Block.solid[l]) {
                byte0 = 2;
            }

            if (Block.solid[j1] && !Block.solid[k1]) {
                byte0 = 5;
            }

            if (Block.solid[k1] && !Block.solid[j1]) {
                byte0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, byte0);
        }
    }
    public BlockAutoCrafter(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityAutoCrafter();
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.isClientSide) {
            TileEntity tile = (TileEntity) world.getBlockTileEntity(x, y, z);
            Catalyst.displayGui(player, tile, "Auto Crafter");
        }
        return true;
    }


    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        int blockId = world.getBlockId(x,y,z);
        if (blockId == PotatoLogisticsMod.blockFurnaceBurner.id || blockId == PotatoLogisticsMod.blockFurnaceBurnerOn.id) {return;}
        TileEntityAutoCrafter tileEntityCrafter = (TileEntityAutoCrafter) world.getBlockTileEntity(x, y, z);
        for (int l = 0; l < tileEntityCrafter.getSizeInventory(); ++l) {
            ItemStack itemstack = tileEntityCrafter.getStackInSlot(l);
            if (itemstack == null) continue;
            float f = this.crafterRand.nextFloat() * 0.8f + 0.1f;
            float f1 = this.crafterRand.nextFloat() * 0.8f + 0.1f;
            float f2 = this.crafterRand.nextFloat() * 0.8f + 0.1f;
            while (itemstack.stackSize > 0) {
                int i1 = this.crafterRand.nextInt(21) + 10;
                if (i1 > itemstack.stackSize) {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)x + f, (float)y + f1, (float)z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
                float f3 = 0.05f;
                entityitem.xd = (float)this.crafterRand.nextGaussian() * f3;
                entityitem.yd = (float)this.crafterRand.nextGaussian() * f3 + 0.2f;
                entityitem.zd = (float)this.crafterRand.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            }
        }
        super.onBlockRemoved(world, x, y, z, data);
    }
}

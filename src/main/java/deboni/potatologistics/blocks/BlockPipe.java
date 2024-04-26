package deboni.potatologistics.blocks;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockPipe extends BlockTileEntity {
    public boolean isDirectional;
    public BlockPipe(String key, int id, Material material, Boolean isDirectional) {
        super(key, id, material);
        this.setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
        this.isDirectional = isDirectional;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityPipe();
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        super.onNeighborBlockChange(world, x, y, z, blockId);
        TileEntityPipe te = (TileEntityPipe)world.getBlockTileEntity(x, y, z);
        if (te != null) {
            te.calcVisualConnections();
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        if (entity instanceof EntityPlayer) {
            if (isDirectional) {
                int meta = side.getDirection().getId() << 3;
                meta |= (1 << 2);

                world.setBlockMetadataWithNotify(x, y, z, meta);
            }
        }
        TileEntityPipe te = (TileEntityPipe)world.getBlockTileEntity(x, y, z);
        if (te != null) {
            te.calcVisualConnections();
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntityPipe te = (TileEntityPipe)world.getBlockTileEntity(x, y, z);
        if (te != null) {
            te.dropItems();
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        TileEntityPipe te = (TileEntityPipe)world.getBlockTileEntity(x, y, z);
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.itemID == PotatoLogisticsMod.itemWrench.id) {
            int meta = world.getBlockMetadata(x, y, z);
            int type = meta & 3;
            type--;
            if (type < 0) type = 2;
            meta = (meta & (~0x03)) | type;
            world.setBlockMetadata(x, y, z, meta);
            world.notifyBlocksOfNeighborChange(x, y, z, this.id);
            world.markBlockNeedsUpdate(x, y, z);
            world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, (double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "random.click", 0.3f, meta == 0 ? 0.5f : 0.6f);

            if (te != null) te.calcVisualConnections();
            return true;
        }
        if (te != null) te.calcVisualConnections();
        return false;
    }

}

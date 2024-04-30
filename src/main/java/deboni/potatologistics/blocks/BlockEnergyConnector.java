package deboni.potatologistics.blocks;

import deboni.potatologistics.Util;
import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityEnergyConnector;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class BlockEnergyConnector extends BlockTileEntity {
    public BlockEnergyConnector(String key, int id, Material material) {
        super(key, id, material);
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
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        world.setBlockMetadataWithNotify(x, y, z, side.getId());
        TileEntity te = Util.getBlockTileEntity(world, x, y, z);
        if (te instanceof TileEntityEnergyConnector) {
            ((TileEntityEnergyConnector) te).updateMachineConnections(side.getOpposite().getDirection());
        }
        super.onBlockPlaced(world, x, y, z, side, entity, sideHeight);
    }

    @Override
    public void setBlockBoundsBasedOnState(World world, int x, int y, int z) {
        Side side = Side.getSideById(world.getBlockMetadata(x, y, z) & 7);
        float pixelSize = 1.0f / 16.0f;
        float min = pixelSize * 5;
        float max = 1 - pixelSize * 5;

        if (side == Side.TOP) {
            this.setBlockBounds(min, 0, min, max, pixelSize * 9, max);
        } else if (side == Side.BOTTOM) {
            this.setBlockBounds(min, 1 - pixelSize * 9, min, max, 1, max);
        } else if (side == Side.NORTH) {
            this.setBlockBounds(min, min, 1 - pixelSize * 9, max, max, 1);
        } else if (side == Side.SOUTH) {
            this.setBlockBounds(min, min, 0, max, max, pixelSize * 9);
        } else if (side == Side.EAST) {
            this.setBlockBounds(0, min, min, pixelSize * 9, max, max);
        } else {
            this.setBlockBounds(1 - pixelSize * 9, min, min, 1, max, max);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        Side side = Side.getSideById(world.getBlockMetadata(x, y, z) & 7).getOpposite();
        Block b = world.getBlock(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ());
        if (b == null) {
            world.setBlockWithNotify(x, y, z, 0);
            world.dropItem(x, y, z, new ItemStack(PotatoLogisticsMod.itemEnergyConnector));
        } else {
            ((TileEntityEnergyConnector) Util.getBlockTileEntity(world, x, y, z)).updateMachineConnections(side.getOpposite().getDirection());
        }
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityEnergyConnector();
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntityEnergyConnector ec = (TileEntityEnergyConnector) world.getBlockTileEntity(x, y, z);
        ItemStack drops = ec.getBreakDrops();
        if (drops != null) world.dropItem(x, y, z, drops);
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        return new ItemStack[]{new ItemStack(PotatoLogisticsMod.itemEnergyConnector)};
    }

}

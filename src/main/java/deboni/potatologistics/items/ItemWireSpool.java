package deboni.potatologistics.items;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.blocks.entities.TileEntityEnergyConnector;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

import java.util.Objects;

public class ItemWireSpool extends Item {

    private String displayName;

    public ItemWireSpool(String name, int id) {
        super(name, id);
        displayName = name;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        Block block = world.getBlock(blockX, blockY, blockZ);
        if (block != null && block.id == PotatoLogisticsMod.blockEnergyConnector.id) {
            boolean connected = itemstack.getData().getBoolean("connected");
            if (connected) {
                TileEntity te = world.getBlockTileEntity(blockX, blockY, blockZ);
                if (te instanceof TileEntityEnergyConnector) {
                    itemstack.getData().putBoolean("connected", false);
                    int x = itemstack.getData().getInteger("x");
                    int y = itemstack.getData().getInteger("y");
                    int z = itemstack.getData().getInteger("z");
                    boolean connectedSuccessfully = ((TileEntityEnergyConnector) te).addConnection(x, y, z);
                    if (connectedSuccessfully) {
                        itemstack.consumeItem(entityplayer);
                    }
                    removedConnectionData(itemstack);
                }
            } else {
                itemstack.getData().putBoolean("connected", true);
                itemstack.getData().putInt("x", blockX);
                itemstack.getData().putInt("y", blockY);
                itemstack.getData().putInt("z", blockZ);
                itemstack.setCustomName(this.displayName + " Connected to: " + blockX + " " + blockY + " " + blockZ);
            }

            return true;
        } else if (entityplayer.isSneaking()) {
            itemstack.getData().putBoolean("connected", false);
            PotatoLogisticsMod.LOGGER.info("Clear spool connection");
            itemstack.setCustomName(this.displayName);
            return true;
        }

        return false;
    }
    private void removedConnectionData(ItemStack stack){
        stack.getData().getValue().remove("connected");
        stack.getData().getValue().remove("x");
        stack.getData().getValue().remove("y");
        stack.getData().getValue().remove("z");
        stack.removeCustomName();
    }
}

package deboni.potatologistics.items;

import net.minecraft.core.item.ItemFood;

public class Potato extends ItemFood {

    public Potato(String name, int id, int healAmount, boolean favouriteWolfMeat) {
        super(name, id, healAmount, favouriteWolfMeat);
    }

    /*
    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if (side != Side.TOP) return false;

        blockY++;
        Block block = PotatoMod.potatoBlock;

        if (!block.canPlaceBlockAt(world, blockX, blockY, blockZ)) return false;
        world.editingBlocks = true;
        world.setBlockWithNotify(blockX, blockY, blockZ, block.id);
        world.editingBlocks = false;
        world.notifyBlocksOfNeighborChange(blockX, blockY, blockZ, block.id);

        itemstack.consumeItem(entityplayer);
        return true;
    }
     */


}

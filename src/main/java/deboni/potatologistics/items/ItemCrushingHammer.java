package deboni.potatologistics.items;

import deboni.potatologistics.PotatoLogisticsMod;
import deboni.potatologistics.Util;
import deboni.potatologistics.blocks.BlockBlockCrusher;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.world.World;

import java.util.HashMap;
import java.util.Map;

public class ItemCrushingHammer extends ItemTool {
    public static Map<Block, Integer> miningLevels = new HashMap<Block, Integer>();
    public ItemCrushingHammer(String name, int id, ToolMaterial toolMaterial) {
        super(name, id, 2, toolMaterial, BlockTags.MINEABLE_BY_PICKAXE);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        Integer miningLevel = miningLevels.get(block);
        if (miningLevel != null) {
            return this.material.getMiningLevel() >= miningLevel;
        }
        return block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) || block.hasTag(BlockTags.MINEABLE_BY_SHOVEL);
    }

    @Override
    public float getStrVsBlock(ItemStack itemstack, Block block) {
        if (block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) || block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
            return this.material.getEfficiency(false);
        }
        return 1.0f;
    }

    @Override
    public boolean onBlockDestroyed(World world, ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
        Block block = Block.blocksList[i];
        if (block != null && (block.getHardness() > 0.0f || this.isSilkTouch())) {
            itemstack.damageItem(1, entityliving);
            PotatoLogisticsMod.LOGGER.info("block destroyed " + block.id);
        }
        return false;
    }

    @Override
    public boolean beforeDestroyBlock(World world, ItemStack itemStack, int x, int y, int z, EntityPlayer player) {
        Block block = world.getBlock(x, y, z);
        ItemStack[] breakResult = BlockBlockCrusher.crushResults.get(block);
        if (breakResult != null) {
            breakResult = Util.cloneStackArray(breakResult);
            for (ItemStack stack : breakResult) {
                world.dropItem(x, y, z, stack);
            }

            world.playSoundEffect(2001, x, y, z, block.id);
            world.setBlockWithNotify(x, y, z, 0);
            return false;
        }
        return true;
    }

    static {
        miningLevels.put(Block.obsidian, 3);
        miningLevels.put(Block.blockDiamond, 2);
        miningLevels.put(Block.oreDiamondStone, 2);
        miningLevels.put(Block.oreDiamondBasalt, 2);
        miningLevels.put(Block.oreDiamondGranite, 2);
        miningLevels.put(Block.oreDiamondLimestone, 2);
        miningLevels.put(Block.blockGold, 2);
        miningLevels.put(Block.oreGoldStone, 2);
        miningLevels.put(Block.oreGoldBasalt, 2);
        miningLevels.put(Block.oreGoldGranite, 2);
        miningLevels.put(Block.oreGoldLimestone, 2);
        miningLevels.put(Block.blockIron, 1);
        miningLevels.put(Block.oreIronStone, 1);
        miningLevels.put(Block.oreIronBasalt, 1);
        miningLevels.put(Block.oreIronGranite, 1);
        miningLevels.put(Block.oreIronLimestone, 1);
        miningLevels.put(Block.blockSteel, 2);
        miningLevels.put(Block.oreNethercoalNetherrack, 2);
        miningLevels.put(Block.blockLapis, 1);
        miningLevels.put(Block.oreLapisStone, 1);
        miningLevels.put(Block.oreLapisBasalt, 1);
        miningLevels.put(Block.oreLapisGranite, 1);
        miningLevels.put(Block.oreLapisLimestone, 1);
        miningLevels.put(Block.blockRedstone, 2);
        miningLevels.put(Block.oreRedstoneStone, 2);
        miningLevels.put(Block.oreRedstoneBasalt, 2);
        miningLevels.put(Block.oreRedstoneGranite, 2);
        miningLevels.put(Block.oreRedstoneLimestone, 2);
        miningLevels.put(Block.oreRedstoneGlowingStone, 2);
        miningLevels.put(Block.oreRedstoneGlowingBasalt, 2);
        miningLevels.put(Block.oreRedstoneGlowingGranite, 2);
        miningLevels.put(Block.oreRedstoneGlowingLimestone, 2);
        miningLevels.put(Block.sand, 1);
        miningLevels.put(Block.gravel, 1);
    }

}

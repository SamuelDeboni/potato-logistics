package deboni.potatologistics;

import deboni.potatologistics.blocks.BlockPipe;
import deboni.potatologistics.blocks.BlockPotato;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import deboni.potatologistics.items.Potato;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemHelper;
import turniplabs.halplibe.helper.RecipeHelper;

public class PotatoLogisticsMod implements ModInitializer {
    public static final String MOD_ID = "potatologistics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Item potato;
    public static Item wrench;
    public static Block potatoBlock;
    public static Block pipe;
    public static Block directionalPipe;
    public static Block itemContainer;

    @Override
    public void onInitialize() {
        LOGGER.info("PotatoLogistics initialized.");

        int blockNum = 999 + 1000;
        //potatoBlock = BlockHelper.createBlock(MOD_ID, new Block("crop.potato", blockNum++, Material.plant), "potato.png", "potato.png", null, 0.0f, 0.0f, 0.0f);
        potatoBlock = new BlockBuilder(MOD_ID)
                .setTextures("potato.png")
                .build(new BlockPotato("block.potato", blockNum++, Material.wood));
        pipe = new BlockBuilder(MOD_ID)
                .setTextures("pipe.png")
                .setLightOpacity(0)
                .build(new BlockPipe("pipe", blockNum++, Material.glass, false));
        directionalPipe = new BlockBuilder(MOD_ID)
                .setTextures("directional_pipe.png")
                .setLightOpacity(0)
                .build(new BlockPipe("directional_pipe", blockNum++, Material.glass, true));
        itemContainer = new BlockBuilder(MOD_ID)
                .setTextures("item_container.png")
                .build(new Block("block.item_container", blockNum++, Material.stone));

        int itemNum = 16999 + 1000;
        potato = ItemHelper.createItem(MOD_ID, new Potato("Potato", itemNum++, 5, true), "potato", "potato.png");
        wrench = ItemHelper.createItem(MOD_ID, new Item("Wrench", itemNum++), "wrench", "wrench.png");
        wrench.setMaxStackSize(1);

        EntityHelper.createTileEntity(TileEntityPipe.class, "pipe.tile");

        RecipeHelper.Crafting.createShapelessRecipe(potato, 1, new Object[]{Item.clay, Item.dustSugar, Item.dustGlowstone});
        RecipeHelper.Crafting.createShapelessRecipe(potato, 9, new Object[]{potatoBlock});
        RecipeHelper.Crafting.createRecipe(potatoBlock, 1, new Object[]{"AAA", "AAA", "AAA", 'A', potato});

        RecipeHelper.Crafting.createRecipe(pipe, 16, new Object[]{"   ", "ABA", "   ", 'A', Item.ingotIron, 'B', Block.glass});
        RecipeHelper.Crafting.createRecipe(directionalPipe, 16, new Object[]{"   ", "ABC", "   ", 'A', Item.ingotIron, 'B', Block.glass, 'C', Item.ingotGold});

        RecipeHelper.Crafting.createRecipe(wrench, 1, new Object[]{" A ", "AA ", "  A", 'A', Item.ingotIron, 'B', Block.glass});
    }
}

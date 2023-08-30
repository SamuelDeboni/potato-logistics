package deboni.potatologistics;

import deboni.potatologistics.blocks.*;
import deboni.potatologistics.blocks.entities.TileEntityAutoBascket;
import deboni.potatologistics.blocks.entities.TileEntityFilter;
import deboni.potatologistics.blocks.entities.TileEntityPipe;
import deboni.potatologistics.items.Potato;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.Texture;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemPlaceable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.*;

public class PotatoLogisticsMod implements ModInitializer {
    public static final String MOD_ID = "potatologistics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Item itemPotato;
    public static Item itemWrench;

    public static Item itemAutoBasket;
    public static Item itemIronGear;
    public static Item itemSteelGear;

    public static Block blockPotato;
    public static Block blockPipe;
    public static Block blockDirectionalPipe;
    public static Block blockFilter;
    public static Block blockAutoBasket;
    public static Block blockBlockCrusher;
    public static Block blockBlockPlacer;
    public static Block blockTreeChoper;
    public static Block blockTreeChopperSaw;

    public static Block blockIronMachineBlock;
    public static Block blockSteelMachineBlock;

    public static Block blockTestAreaMaker;

    @Override
    public void onInitialize() {
        LOGGER.info("PotatoLogistics initialized.");

        int blockNum = 999 + 1000;
        //potatoBlock = BlockHelper.createBlock(MOD_ID, new Block("crop.potato", blockNum++, Material.plant), "potato.png", "potato.png", null, 0.0f, 0.0f, 0.0f);
        blockPotato = new BlockBuilder(MOD_ID)
                .setTextures("potato.png")
                .build(new BlockPotato("potato", blockNum++, Material.wood));
        blockPipe = new BlockBuilder(MOD_ID)
                .setTextures("pipe.png")
                .setLightOpacity(0)
                .build(new BlockPipe("pipe", blockNum++, Material.glass, false));
        blockDirectionalPipe = new BlockBuilder(MOD_ID)
                .setTextures("directional_pipe.png")
                .setLightOpacity(0)
                .build(new BlockPipe("directional_pipe", blockNum++, Material.glass, true));

        blockFilter = new BlockBuilder(MOD_ID)
                .setTextures("block_filter.png")
                .setLightOpacity(0)
                .build(new BlockFilter("filter", blockNum++, Material.wood));

        blockAutoBasket = new BlockBuilder(MOD_ID)
                .setTopTexture(4, 9)
                .setBottomTexture("auto_basket_bottom.png")
                .setSideTextures("auto_basket_sides.png")
                .setLightOpacity(0)
                .build(new BlockAutoBasket("auto_basket", blockNum++, Material.cloth));

        blockBlockCrusher = new BlockBuilder(MOD_ID)
                .setSideTextures(14, 3)
                .setTopTexture("block_crusher_front.png")
                .setBottomTexture("block_crusher_back.png")
                .build(new BlockBlockCrusher("block_crusher", blockNum++, Material.stone));

        blockBlockPlacer = new BlockBuilder(MOD_ID)
                .setSideTextures(14, 3)
                .setTopTexture("block_placer_front.png")
                .setBottomTexture("block_crusher_back.png")
                .build(new BlockBlockPlacer("block_placer", blockNum++, Material.stone));

        blockTreeChoper = new BlockBuilder(MOD_ID)
                .setSideTextures("iron_machine_block.png")
                .setTopTexture("tree_choper_front.png")
                .setBottomTexture("iron_machine_out.png")
                .setLightOpacity(0)
                .build(new BlockTreeChopper("tree_chopper", blockNum++, Material.metal));

        blockTreeChopperSaw = new BlockBuilder(MOD_ID)
                .setTextures("tree_chopper_saw.png")
                .build(new BlockTreeChopper("tree_chopper_saw", blockNum++, Material.metal));

        blockIronMachineBlock = new BlockBuilder(MOD_ID)
                .setTextures("iron_machine_block.png")
                .build(new Block("iron_machine_block", blockNum++, Material.metal));

        blockSteelMachineBlock = new BlockBuilder(MOD_ID)
                .setTextures("steel_machine_block.png")
                .build(new Block("steel_machine_block", blockNum++, Material.metal));

        blockTestAreaMaker = new BlockBuilder(MOD_ID)
                .setTextures("potato.png")
                .build(new BlockTestAreaMaker("test_area_maker", blockNum++, Material.metal));

        int itemNum = 16999 + 1000;
        itemPotato = ItemHelper.createItem(MOD_ID, new Potato("Potato", itemNum++, 5, true), "potato", "potato.png");
        itemWrench = ItemHelper.createItem(MOD_ID, new Item("Wrench", itemNum++), "wrench", "wrench.png");
        itemWrench.setMaxStackSize(1);
        itemAutoBasket = ItemHelper.createItem(MOD_ID, new ItemPlaceable("Auto Basket", itemNum++, blockAutoBasket), "auto_basket", "auto_basket.png");

        itemIronGear = ItemHelper.createItem(MOD_ID, new Item("Iron Gear", itemNum++), "iron_gear", "iron_gear.png");
        itemSteelGear = ItemHelper.createItem(MOD_ID, new Item("Steel Gear", itemNum++), "steel_gear", "steel_gear.png");


        EntityHelper.createSpecialTileEntity(TileEntityPipe.class, new TileEntityRendererPipe(), "pipe.tile");
        EntityHelper.createTileEntity(TileEntityFilter.class, "filter.tile");
        EntityHelper.createTileEntity(TileEntityAutoBascket.class, "auto_basket.tile");

        RecipeHelper.Crafting.createShapelessRecipe(itemPotato, 1, new Object[]{Item.clay, Item.dustSugar, Item.dustGlowstone});
        RecipeHelper.Crafting.createShapelessRecipe(itemPotato, 9, new Object[]{blockPotato});
        RecipeHelper.Crafting.createRecipe(blockPotato, 1, new Object[]{"AAA", "AAA", "AAA", 'A', itemPotato});

        RecipeHelper.Crafting.createRecipe(blockPipe, 16, new Object[]{"   ", "ABA", "   ", 'A', Item.ingotIron, 'B', Block.glass});
        RecipeHelper.Crafting.createRecipe(blockDirectionalPipe, 16, new Object[]{"   ", "ABC", "   ", 'A', Item.ingotIron, 'B', Block.glass, 'C', Item.ingotGold});

        RecipeHelper.Crafting.createRecipe(itemWrench, 1, new Object[]{" A ", "AA ", "  A", 'A', Item.ingotIron, 'B', Block.glass});

        RecipeHelper.Crafting.createRecipe(blockFilter, 1, new Object[]{"ABA", "BCB", "ABA", 'A', Block.planksOak, 'B', Item.dustRedstone, 'C', Block.mesh});
        RecipeHelper.Crafting.createRecipe(itemAutoBasket, 1, new Object[]{"AAA", "CBC", "CCC", 'A', Item.leather, 'B', Item.dustRedstone, 'C', Item.wheat});
        RecipeHelper.Crafting.createRecipe(blockBlockCrusher, 1, new Object[]{"ABA", "ECF", "ADA", 'A', Block.cobbleStone, 'B', Block.obsidian, 'C', Item.toolPickaxeDiamond, 'D', Block.pistonBaseSticky, 'E', blockPipe, 'F', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(blockBlockPlacer, 1, new Object[]{"ADA", "ACA", "ABA", 'A', Block.cobbleStone, 'B', blockPipe, 'C', Item.dustRedstone, 'D', Block.pistonBase});
        RecipeHelper.Crafting.createRecipe(blockTreeChoper, 1, new Object[]{"ABA", "ECF", "ADA", 'A', Block.cobbleStone, 'B', Block.obsidian, 'C', Item.toolAxeDiamond, 'D', Block.pistonBaseSticky, 'E', blockPipe, 'F', Item.dustRedstone});

    }
}

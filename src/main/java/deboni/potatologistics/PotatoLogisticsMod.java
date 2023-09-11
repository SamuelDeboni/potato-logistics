package deboni.potatologistics;

import deboni.potatologistics.blocks.*;
import deboni.potatologistics.blocks.entities.*;
import deboni.potatologistics.items.ItemWireSpool;
import deboni.potatologistics.items.Potato;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemPlaceable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.energyapi.EnergyAPI;
import sunsetsatellite.sunsetutils.util.Config;
import turniplabs.halplibe.helper.*;

public class PotatoLogisticsMod implements ModInitializer {
    public static final String MOD_ID = "potatologistics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Item itemPotato;
    public static Item itemWrench;

    public static Item itemAutoBasket;
    public static Item itemIronGear;
    public static Item itemSteelGear;
    public static Item itemEnergyConnector;
    public static Item itemWireSpool;
    public static  Item itemRedstoneAlloy;
    public static  Item itemRedstoneIronMix;

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
    public static Block blockMiningDrill;
    public static Block blockEnergyConnector;
    public static Block blockAdvancedDispenser;

    public static Block blockFurnaceBurner;
    public static Block blockStirlingEngine;
    public static Block blockCoil;

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
                .setHardness(0.1f)
                .build(new BlockPipe("pipe", blockNum++, Material.glass, false));
        blockDirectionalPipe = new BlockBuilder(MOD_ID)
                .setTextures("directional_pipe.png")
                .setLightOpacity(0)
                .setHardness(0.1f)
                .build(new BlockPipe("directional_pipe", blockNum++, Material.glass, true));

        blockFilter = new BlockBuilder(MOD_ID)
                .setTextures("block_filter.png")
                .setLightOpacity(0)
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_AXE)
                .build(new BlockFilter("filter", blockNum++, Material.wood));

        blockAutoBasket = new BlockBuilder(MOD_ID)
                .setTopTexture(4, 9)
                .setBottomTexture("auto_basket_bottom.png")
                .setSideTextures("auto_basket_sides.png")
                .setLightOpacity(0)
                .setHardness(0.1f)
                .build(new BlockAutoBasket("auto_basket", blockNum++, Material.cloth));

        blockBlockCrusher = new BlockBuilder(MOD_ID)
                .setSideTextures(14, 3)
                .setTopTexture("block_crusher_front.png")
                .setBottomTexture("block_crusher_back.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockBlockCrusher("block_crusher", blockNum++, Material.stone));

        blockBlockPlacer = new BlockBuilder(MOD_ID)
                .setSideTextures(14, 3)
                .setTopTexture("block_placer_front.png")
                .setBottomTexture("block_crusher_back.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockBlockPlacer("block_placer", blockNum++, Material.stone));

        blockTreeChoper = new BlockBuilder(MOD_ID)
                .setSideTextures("iron_machine_side.png")
                .setNorthTexture("iron_machine_block.png")
                .setTopTexture("tree_choper_front.png")
                .setBottomTexture("iron_machine_out.png")
                .setLightOpacity(0)
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockTreeChopper("tree_chopper", blockNum++, Material.metal));

        blockTreeChopperSaw = new BlockBuilder(MOD_ID)
                .setTextures("tree_chopper_saw.png")
                .setTags(BlockTags.NOT_IN_CREATIVE_MENU)
                .build(new BlockTreeChopper("tree_chopper_saw", blockNum++, Material.metal));

        blockIronMachineBlock = new BlockBuilder(MOD_ID)
                .setTextures("iron_machine_block.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new Block("iron_machine_block", blockNum++, Material.metal));

        blockSteelMachineBlock = new BlockBuilder(MOD_ID)
                .setTextures("steel_machine_block.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new Block("steel_machine_block", blockNum++, Material.metal));

        blockTestAreaMaker = new BlockBuilder(MOD_ID)
                .setTextures("potato.png")
                .build(new BlockTestAreaMaker("test_area_maker", blockNum++, Material.metal));

        blockMiningDrill = new BlockBuilder(MOD_ID)
                .setSideTextures("mining_drill_sides.png")
                .setTopTexture("mining_drill_top.png")
                .setBottomTexture("mining_drill_bottom.png")
                .setLightOpacity(0)
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockMiningDrill("mining_drill", blockNum++, Material.metal));

        blockEnergyConnector = new BlockBuilder(MOD_ID)
                .setTextures("energy_connector.png")
                .setLightOpacity(0)
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.NOT_IN_CREATIVE_MENU)
                .build(new BlockEnergyConnector("energy_connector", blockNum++, Material.metal));

        blockAdvancedDispenser = new BlockBuilder(MOD_ID)
                .setTextures("iron_machine_side.png")
                .setTopTexture("iron_machine_block.png")
                .setBottomTexture("iron_machine_block.png")
                .setNorthTexture("advanced_dispenser_front.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockAdvancedDispenser("advanced_dispenser", blockNum++));

        blockFurnaceBurner = new BlockBuilder(MOD_ID)
                .setTopBottomTexture("iron_machine_block.png")
                .setSideTextures("furnace_burner.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockFurnaceBurner("furnace_burner", blockNum++, Material.metal));

        blockStirlingEngine = new BlockBuilder(MOD_ID)
                .setTextures("iron_machine_block.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockStirlingEngine("stirling_engine", blockNum++, Material.metal));

        blockCoil = new BlockBuilder(MOD_ID)
                .setTopTexture("coil_block_top.png")
                .setBottomTexture("coil_block_bottom.png")
                .setSideTextures("coil_block_sides.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new Block("coil", blockNum++, Material.metal));

        int itemNum = 16999 + 1000;
        itemPotato = ItemHelper.createItem(MOD_ID, new Potato("Potato", itemNum++, 5, true), "potato", "potato.png");
        itemWrench = ItemHelper.createItem(MOD_ID, new Item("Wrench", itemNum++), "wrench", "wrench.png");
        itemWrench.setMaxStackSize(1);
        itemAutoBasket = ItemHelper.createItem(MOD_ID, new ItemPlaceable("Auto Basket", itemNum++, blockAutoBasket), "auto_basket", "auto_basket.png");

        itemIronGear = ItemHelper.createItem(MOD_ID, new Item("Iron Gear", itemNum++), "iron_gear", "iron_gear.png");
        itemSteelGear = ItemHelper.createItem(MOD_ID, new Item("Steel Gear", itemNum++), "steel_gear", "steel_gear.png");
        itemEnergyConnector = ItemHelper.createItem(MOD_ID, new ItemPlaceable("Energy Connector", itemNum++, blockEnergyConnector), "energy_connector", "energy_connector.png");
        itemWireSpool = ItemHelper.createItem(MOD_ID, new ItemWireSpool("Wire Spool", itemNum++), "wire_spool", "wire_spool.png");
        itemRedstoneAlloy = ItemHelper.createItem(MOD_ID, new Item("Redstone Alloy", itemNum++), "redstone_alloy", "redstone_alloy.png");
        itemRedstoneIronMix = ItemHelper.createItem(MOD_ID, new Item("Redstone Iron Mix", itemNum++), "redstone_iron_mix", "redstone_iron_mix.png");

        EntityHelper.createSpecialTileEntity(TileEntityPipe.class, new TileEntityRendererPipe(), "pipe.tile");
        EntityHelper.createTileEntity(TileEntityFilter.class, "filter.tile");
        EntityHelper.createTileEntity(TileEntityAutoBascket.class, "auto_basket.tile");
        EntityHelper.createTileEntity(TileEntiyTreeChopper.class, "tree_chopper.tile");
        EntityHelper.createTileEntity(TileEntityStirlingEngine.class, "stirling_engine.tile");
        EntityHelper.createTileEntity(TileEntityFurnaceBurner.class, "furnace_burner.tile");
        EntityHelper.createSpecialTileEntity(TileEntityMiningDrill.class, new TileEntityRendererMiningDrill(), "mining_drill.tile");
        EntityHelper.createSpecialTileEntity(TileEntityEnergyConnector.class, new TileEntityRendererEnergyConnector(), "energy_connector.tile");

        RecipeHelper.Crafting.createShapelessRecipe(itemPotato, 1, new Object[]{Item.clay, Item.dustSugar, Item.dustGlowstone});
        RecipeHelper.Crafting.createShapelessRecipe(itemPotato, 9, new Object[]{blockPotato});
        RecipeHelper.Crafting.createShapelessRecipe(itemWireSpool, 1, new Object[]{itemWireSpool});
        RecipeHelper.Crafting.createShapelessRecipe(itemRedstoneIronMix, 1, new Object[]{Item.ingotIron, Item.dustRedstone, Item.dustRedstone, Item.dustRedstone});

        RecipeHelper.Crafting.createRecipe(blockPotato, 1, new Object[]{"AAA", "AAA", "AAA", 'A', itemPotato});
        RecipeHelper.Crafting.createRecipe(blockPipe, 16, new Object[]{"   ", "ABA", "   ", 'A', Item.ingotIron, 'B', Block.glass});
        RecipeHelper.Crafting.createRecipe(blockDirectionalPipe, 16, new Object[]{"   ", "ABC", "   ", 'A', Item.ingotIron, 'B', Block.glass, 'C', Item.ingotGold});
        RecipeHelper.Crafting.createRecipe(itemWrench, 1, new Object[]{" A ", "AA ", "  A", 'A', Item.ingotIron, 'B', Block.glass});
        RecipeHelper.Crafting.createRecipe(blockFilter, 1, new Object[]{"ABA", "BCB", "ABA", 'A', Block.planksOak, 'B', Item.dustRedstone, 'C', Block.mesh});
        RecipeHelper.Crafting.createRecipe(itemAutoBasket, 1, new Object[]{"AAA", "CBC", "CCC", 'A', Item.leather, 'B', Item.dustRedstone, 'C', Item.wheat});
        RecipeHelper.Crafting.createRecipe(blockBlockCrusher, 1, new Object[]{"ABA", "ECF", "ADA", 'A', Block.cobbleStone, 'B', Block.obsidian, 'C', Item.toolPickaxeDiamond, 'D', Block.pistonBaseSticky, 'E', blockPipe, 'F', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(blockBlockPlacer, 1, new Object[]{"ADA", "ACA", "ABA", 'A', Block.cobbleStone, 'B', blockPipe, 'C', Item.dustRedstone, 'D', Block.pistonBase});
        RecipeHelper.Crafting.createRecipe(blockTreeChoper, 1, new Object[]{"AAA", "BCD", "AEA", 'A', Item.ingotIron, 'B', Item.toolAxeDiamond, 'C', blockIronMachineBlock, 'D', blockPipe, 'E', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(blockMiningDrill, 1, new Object[]{"ADA", "CBC", "AEA", 'A', Item.ingotSteel, 'B', Item.toolPickaxeDiamond, 'C', blockSteelMachineBlock, 'D', blockPipe, 'E', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(itemIronGear, 1, new Object[]{" A ", "A A", " A ", 'A', Item.ingotIron});
        RecipeHelper.Crafting.createRecipe(itemSteelGear, 1, new Object[]{" A ", "A A", " A ", 'A', Item.ingotSteel});
        RecipeHelper.Crafting.createRecipe(blockIronMachineBlock, 1, new Object[]{"AAA", "BCB", "AAA", 'A', Item.ingotIron, 'B', itemIronGear, 'C', itemRedstoneAlloy});
        RecipeHelper.Crafting.createRecipe(blockSteelMachineBlock, 1, new Object[]{"AAA", "BCB", "AAA", 'A', Item.ingotSteel, 'B', itemSteelGear, 'C', itemRedstoneAlloy});
        RecipeHelper.Crafting.createRecipe(itemWireSpool, 8, new Object[]{" A ", "ABA", " A ", 'A', itemRedstoneAlloy, 'B', Item.stick});
        RecipeHelper.Crafting.createRecipe(itemEnergyConnector, 4, new Object[]{" A ", "BAB", "BAB", 'A', Item.ingotIron, 'B', Item.brickClay});

        RecipeHelper.Smelting.createRecipe(itemRedstoneAlloy, itemRedstoneIronMix);

        if (EnergyAPI.generator != null) {
            RecipeHelper.Crafting.createRecipe(EnergyAPI.generator, 1, new Object[]{"AAA", "ABA", "ACA", 'A', Item.ingotIron, 'B', blockIronMachineBlock, 'C', Block.furnaceStoneIdle});
        }
    }
}

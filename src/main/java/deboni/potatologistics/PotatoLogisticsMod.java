package deboni.potatologistics;

import deboni.potatologistics.blocks.*;
import deboni.potatologistics.blocks.entities.*;
import deboni.potatologistics.gui.*;
import deboni.potatologistics.items.ItemWireSpool;
import deboni.potatologistics.items.Potato;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemPlaceable;
import net.minecraft.core.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.MpGuiEntry;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.util.Properties;

public class PotatoLogisticsMod implements ModInitializer, GameStartEntrypoint, ClientStartEntrypoint {
    public static final String MOD_ID = "potatologistics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ConfigHandler config;
    static {
        Properties prop = new Properties();
        prop.setProperty("starting_block_id","1999");
        prop.setProperty("starting_item_id","17999");
        config = new ConfigHandler(MOD_ID,prop);
        config.updateConfig();
    }
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
    public static Block blockTreeChopper;
    public static Block blockTreeChopperSaw;

    public static Block blockIronMachineBlock;
    public static Block blockSteelMachineBlock;

    public static Block blockTestAreaMaker;
    public static Block blockMiningDrill;
    public static Block blockEnergyConnector;
    public static Block blockAdvancedDispenser;

    public static Block blockFurnaceBurner;
    public static Block blockFurnaceBurnerOn;
    public static Block blockStirlingEngine;
    public static Block blockCoil;
    public static Block blockAutoCrafter;
    public static Block blockCapacitorLv;

    @Override
    public void onInitialize() {
        LOGGER.info("PotatoLogistics initialized.");

        int blockNum = config.getInt("starting_block_id");
        //potatoBlock = BlockHelper.createBlock(MOD_ID, new Block("crop.potato", blockNum++, Material.plant), "potato.png", "potato.png", null, 0.0f, 0.0f, 0.0f);
        blockPotato = new BlockBuilder(MOD_ID)
                .setTextures("potato.png")
                .build(new BlockPotato("potato", blockNum++, Material.wood));
        blockPipe = new BlockBuilder(MOD_ID)
                .setTextures("pipe.png")
                .setLightOpacity(0)
                .setHardness(0.1f)
                .setBlockModel(new BlockModelRenderBlocks(151))
                .build(new BlockPipe("pipe", blockNum++, Material.glass, false));
        blockDirectionalPipe = new BlockBuilder(MOD_ID)
                .setTextures("directional_pipe.png")
                .setLightOpacity(0)
                .setHardness(0.1f)
                .setBlockModel(new BlockModelRenderBlocks(151))
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
                .setBlockModel(new BlockModelRenderBlocks(150))
                .build(new BlockAutoBasket("auto_basket", blockNum++, Material.cloth));

        blockBlockCrusher = new BlockBuilder(MOD_ID)
                .setSideTextures("block_crusher_side.png")
                .setTopTexture("block_crusher_front.png")
                .setBottomTexture("block_crusher_back.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockBlockCrusher("block_crusher", blockNum++, Material.stone));

        blockBlockPlacer = new BlockBuilder(MOD_ID)
                .setSideTextures("block_placer_side.png")
                .setTopTexture("block_placer_front.png")
                .setBottomTexture("block_crusher_back.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockBlockPlacer("block_placer", blockNum++, Material.stone));

        blockTreeChopper = new BlockBuilder(MOD_ID)
                .setSideTextures("iron_machine_side.png")
                .setNorthTexture("iron_machine_block.png")
                .setSouthTexture("iron_chasing_details1.png")
                .setTopTexture("tree_chopper_front.png")
                .setBottomTexture("iron_machine_out.png")
                .setLightOpacity(0)
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .setBlockModel(new BlockModelRenderBlocks(152))
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
                .setBlockModel(new BlockModelRenderBlocks(153))
                .build(new BlockEnergyConnector("energy_connector", blockNum++, Material.metal));

        blockAdvancedDispenser = new BlockBuilder(MOD_ID)
                .setTextures("iron_machine_side.png")
                .setTopTexture("iron_chasing_details0.png")
                .setBottomTexture("iron_chasing_details1.png")
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

        blockFurnaceBurnerOn = new BlockBuilder(MOD_ID)
                .setTopBottomTexture("iron_machine_block.png")
                .setSideTextures("furnace_burner_on.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.NOT_IN_CREATIVE_MENU)
                .build(new BlockFurnaceBurner("furnace_burner_on", blockNum++, Material.metal));

        blockStirlingEngine = new BlockBuilder(MOD_ID)
                .setBottomTexture("stirling_engine_bottom.png")
                .setTopTexture("stirling_engine_top.png")
                .setSideTextures("stirling_engine_sides.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .setBlockModel(new BlockModelRenderBlocks(154))
                .build(new BlockStirlingEngine("stirling_engine", blockNum++, Material.metal));

        blockCoil = new BlockBuilder(MOD_ID)
                .setTopTexture("coil_block_top.png")
                .setBottomTexture("coil_block_bottom.png")
                .setSideTextures("coil_block_sides.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockCoil("coil", blockNum++, Material.metal));

        blockAutoCrafter = new BlockBuilder(MOD_ID)
                .setTextures("iron_machine_side.png")
                .setTopTexture("auto_crafter_top.png")
                .setBottomTexture("iron_machine_block.png")
                .setNorthTexture("auto_crafter_front.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockAutoCrafter("auto_crafter", blockNum++, Material.metal));

        blockCapacitorLv = new BlockBuilder(MOD_ID)
                .setTextures("capacitor_out.png")
                .setTopTexture("capacitor_in.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockCapacitor("lv_capacitor", blockNum++, Material.metal));

        int itemNum = config.getInt("starting_item_id");
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

        RecipeHelper.Crafting.createShapelessRecipe(itemPotato, 1, new Object[]{Item.clay, Item.dustSugar, Item.dustGlowstone});
        RecipeHelper.Crafting.createShapelessRecipe(itemPotato, 9, new Object[]{blockPotato});
        RecipeHelper.Crafting.createShapelessRecipe(itemWireSpool, 1, new Object[]{itemWireSpool});
        RecipeHelper.Crafting.createShapelessRecipe(itemRedstoneIronMix, 1, new Object[]{Item.ingotIron, Item.dustRedstone, Item.dustRedstone, Item.dustRedstone});

        RecipeHelper.Crafting.createRecipe(new ItemStack(blockPotato, 1), new Object[]{"AAA", "AAA", "AAA", 'A', itemPotato});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockPipe, 16), new Object[]{"   ", "ABA", "   ", 'A', Item.ingotIron, 'B', Block.glass});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockDirectionalPipe, 16), new Object[]{"   ", "ABC", "   ", 'A', Item.ingotIron, 'B', Block.glass, 'C', Item.ingotGold});
        RecipeHelper.Crafting.createRecipe(new ItemStack(itemWrench, 1), new Object[]{" A ", "AA ", "  A", 'A', Item.ingotIron, 'B', Block.glass});
        RecipeHelper.craftingManager.addRecipe(new ItemStack(blockFilter, 1), true, false,
                new Object[]{"ABA", "BCB", "ABA", 'A', Block.planksOak, 'B', Item.dustRedstone, 'C', Block.mesh});

        //RecipeHelper.Crafting.createRecipe(blockFilter, 1, new Object[]{"ABA", "BCB", "ABA", 'A', Block.planksOakPainted, 'B', Item.dustRedstone, 'C', Block.mesh});
        //RecipeHelper.Crafting.createRecipe(blockFilter, 1, new Object[]{"ABA", "BCB", "ABA", 'A', new ItemStack(Block.planksOakPainted.id, 1, 1), 'B', Item.dustRedstone, 'C', Block.mesh});
        RecipeHelper.Crafting.createRecipe(new ItemStack(itemAutoBasket, 1), new Object[]{"AAA", "CBC", "CCC", 'A', Item.leather, 'B', Item.dustRedstone, 'C', Item.wheat});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockBlockCrusher, 1), new Object[]{"ABA", "ECF", "ADA", 'A', Block.cobbleStone, 'B', Block.obsidian, 'C', Item.toolPickaxeDiamond, 'D', Block.pistonBaseSticky, 'E', blockPipe, 'F', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockBlockPlacer, 1), new Object[]{"ADA", "ACA", "ABA", 'A', Block.cobbleStone, 'B', blockPipe, 'C', Item.dustRedstone, 'D', Block.pistonBase});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockTreeChopper, 1), new Object[]{"AAA", "BCD", "AEA", 'A', Item.ingotIron, 'B', Item.toolAxeDiamond, 'C', blockIronMachineBlock, 'D', blockPipe, 'E', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockMiningDrill, 1), new Object[]{"ADA", "CBC", "AEA", 'A', Item.ingotSteel, 'B', Item.toolPickaxeDiamond, 'C', blockSteelMachineBlock, 'D', blockPipe, 'E', Item.dustRedstone});
        RecipeHelper.Crafting.createRecipe(new ItemStack(itemIronGear, 1), new Object[]{" A ", "A A", " A ", 'A', Item.ingotIron});
        RecipeHelper.Crafting.createRecipe(new ItemStack(itemSteelGear, 1), new Object[]{" A ", "A A", " A ", 'A', Item.ingotSteel});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockIronMachineBlock, 1), new Object[]{"AAA", "BCB", "AAA", 'A', Item.ingotIron, 'B', itemIronGear, 'C', itemRedstoneAlloy});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockSteelMachineBlock, 1), new Object[]{"AAA", "BCB", "AAA", 'A', Item.ingotSteel, 'B', itemSteelGear, 'C', itemRedstoneAlloy});
        RecipeHelper.Crafting.createRecipe(new ItemStack(itemWireSpool, 4), new Object[]{" A ", "ABA", " A ", 'A', itemRedstoneAlloy, 'B', Item.stick});
        RecipeHelper.Crafting.createRecipe(new ItemStack(itemEnergyConnector, 4), new Object[]{" A ", "BAB", "BAB", 'A', Item.ingotIron, 'B', Item.brickClay});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockAutoCrafter, 1), new Object[]{"RRR", "ICI", "IGI", 'R', Item.dustRedstone, 'I', Item.ingotIron, 'C', Block.workbench, 'G', itemIronGear});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockFurnaceBurner, 1), new Object[]{"III", "I I", "IFI", 'I', Item.ingotIron, 'F', Block.furnaceStoneIdle});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockStirlingEngine, 1), new Object[]{"IPI", " M ", "IPI", 'I', Item.ingotIron, 'P', Block.pistonBase, 'M', blockIronMachineBlock});
        RecipeHelper.Crafting.createRecipe(new ItemStack(blockCoil, 1), new Object[]{"WWW", "W W", "WWW", 'W', itemWireSpool});

        RecipeHelper.Smelting.createRecipe(itemRedstoneAlloy, itemRedstoneIronMix);
    }

    @Override
    public void beforeGameStart() {
        EntityHelper.Core.createTileEntity(TileEntityPipe.class, "pipe.tile");

        EntityHelper.Core.createTileEntity(TileEntityFilter.class, "filter.tile");
        Catalyst.GUIS.register("Filter", new MpGuiEntry(TileEntityFilter.class, GuiFilter.class, ContainerFilter.class));

        EntityHelper.Core.createTileEntity(TileEntityBurner.class, "furnace_burner.tile");
        Catalyst.GUIS.register("Coal Burner", new MpGuiEntry(TileEntityBurner.class, GuiBurner.class, ContainerBurner.class));

        EntityHelper.Core.createTileEntity(TileEntityAutoBasket.class, "auto_basket.tile");
        EntityHelper.Core.createTileEntity(TileEntityTreeChopper.class, "tree_chopper.tile");
        EntityHelper.Core.createTileEntity(TileEntityStirlingEngine.class, "stirling_engine.tile");

        EntityHelper.Core.createTileEntity(TileEntityCoil.class, "coil.tile");
        EntityHelper.Core.createTileEntity(TileEntityMiningDrill.class, "mining_drill.tile");
        EntityHelper.Core.createTileEntity(TileEntityEnergyConnector.class, "energy_connector.tile");

        EntityHelper.Core.createTileEntity(TileEntityAutoCrafter.class, "auto_crafter.tile");
        Catalyst.GUIS.register("Auto Crafter", new MpGuiEntry(TileEntityAutoCrafter.class, GuiAutoCrafter.class, ContainerAutoCrafter.class));

        EntityHelper.Core.createTileEntity(TileEntityCapacitor.class, "capacitor.tile");
    }
    @Override
    public void beforeClientStart() {
        EntityHelper.Client.assignTileEntityRenderer(TileEntityPipe.class, new TileEntityRendererPipe());
        EntityHelper.Client.assignTileEntityRenderer(TileEntityMiningDrill.class, new TileEntityRendererMiningDrill());
        EntityHelper.Client.assignTileEntityRenderer(TileEntityEnergyConnector.class, new TileEntityRendererEnergyConnector());
    }

    @Override
    public void afterGameStart() {

    }

    @Override
    public void afterClientStart() {

    }
}

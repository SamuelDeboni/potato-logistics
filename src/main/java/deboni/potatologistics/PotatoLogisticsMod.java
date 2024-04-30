package deboni.potatologistics;

import deboni.potatologistics.blocks.*;
import deboni.potatologistics.blocks.entities.*;
import deboni.potatologistics.gui.*;
import deboni.potatologistics.items.ItemCrushingHammer;
import deboni.potatologistics.items.ItemWireSpool;
import deboni.potatologistics.items.Potato;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockSand;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemPlaceable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.sound.BlockSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.MpGuiEntry;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Arrays;
import java.util.Properties;

public class PotatoLogisticsMod implements ModInitializer, GameStartEntrypoint, ClientStartEntrypoint, RecipeEntrypoint {
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
    public static Item itemRedstoneAlloy;
    public static Item itemRedstoneIronMix;

    public static Item toolWoodHammer;
    public static Item toolStoneHammer;
    public static Item toolIronHammer;
    public static Item toolDiamondHammer;
    public static Item toolSteelHammer;

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
    public static Block blockHeater;
    public static Block blockStirlingEngineMV;
    public static Block blockDust;


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
                .setBottomTexture("block_placer_back.png")
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
                .build(new BlockStirlingEngine("stirling_engine_lv", blockNum++, Material.metal));

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
                .setResistance(5)
                .setLuminance(1)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .setBlockModel(new BlockModelRenderBlocks(155))
                .build(new BlockCapacitor("capacitor", blockNum++, Material.metal));

        blockHeater = new BlockBuilder(MOD_ID)
                .setTextures("heater_sides.png")
                .setTopTexture("heater_top.png")
                .setBottomTexture("iron_machine_block.png")
                .setHardness(1.5f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .build(new BlockHeater("heater", blockNum++, Material.metal));

        blockStirlingEngineMV = new BlockBuilder(MOD_ID)
                .setBottomTexture("stirling_engine_mv_bottom.png")
                .setTopTexture("stirling_engine_mv_top.png")
                .setSideTextures("stirling_engine_mv_sides.png")
                .setHardness(2.0f)
                .setTags(BlockTags.MINEABLE_BY_PICKAXE)
                .setBlockModel(new BlockModelRenderBlocks(154))
                .build(new BlockStirlingEngineMV("stirling_engine_mv", blockNum++, Material.metal));

        blockDust = new BlockBuilder(MOD_ID)
                .setTextures("dust.png")
                .setHardness(1.0f)
                .setTags(BlockTags.MINEABLE_BY_SHOVEL)
                .setBlockSound(BlockSounds.SAND)
                .build(new BlockSand("dust", blockNum++));

        BlockBlockCrusher.crushResults.put(Block.sand, new ItemStack[]{new ItemStack(blockDust)});

        int itemNum = config.getInt("starting_item_id");
        itemPotato = ItemHelper.createItem(MOD_ID, new Potato("Potato", itemNum++, 5, true).setKey("potato"), "potato.png");
        itemWrench = ItemHelper.createItem(MOD_ID, new Item("Wrench", itemNum++).setKey("wrench"), "wrench.png");
        itemWrench.setMaxStackSize(1);
        itemAutoBasket = ItemHelper.createItem(MOD_ID, new ItemPlaceable("Auto Basket", itemNum++, blockAutoBasket).setKey("auto_basket"), "auto_basket.png");

        itemIronGear = ItemHelper.createItem(MOD_ID, new Item("Iron Gear", itemNum++).setKey("iron_gear"), "iron_gear.png");
        itemSteelGear = ItemHelper.createItem(MOD_ID, new Item("Steel Gear", itemNum++).setKey("steel_gear"), "steel_gear.png");
        itemEnergyConnector = ItemHelper.createItem(MOD_ID, new ItemPlaceable("Energy Connector", itemNum++, blockEnergyConnector).setKey("energy_connector"), "energy_connector.png");
        itemWireSpool = ItemHelper.createItem(MOD_ID, new ItemWireSpool("Wire Spool", itemNum++).setKey("wire_spool"), "wire_spool.png");
        itemRedstoneAlloy = ItemHelper.createItem(MOD_ID, new Item("Redstone Alloy", itemNum++).setKey("redstone_alloy"), "redstone_alloy.png");
        itemRedstoneIronMix = ItemHelper.createItem(MOD_ID, new Item("Redstone Iron Mix", itemNum++).setKey("redstone_iron_mix"), "redstone_iron_mix.png");

        toolWoodHammer = ItemHelper.createItem(MOD_ID, new ItemCrushingHammer("Wood Crushing Hammer", itemNum++, ToolMaterial.wood).setKey("wood_crushing_hammer"), "wood_hammer.png");
        toolStoneHammer = ItemHelper.createItem(MOD_ID, new ItemCrushingHammer("Stone Crushing Hammer", itemNum++, ToolMaterial.stone).setKey("stone_crushing_hammer"), "stone_hammer.png");
        toolIronHammer = ItemHelper.createItem(MOD_ID, new ItemCrushingHammer("Iron Crushing Hammer", itemNum++, ToolMaterial.iron).setKey("iron_crushing_hammer"), "iron_hammer.png");
        toolDiamondHammer = ItemHelper.createItem(MOD_ID, new ItemCrushingHammer("Diamond Crushing Hammer", itemNum++, ToolMaterial.diamond).setKey("diamond_crushing_hammer"), "diamond_hammer.png");
        toolSteelHammer = ItemHelper.createItem(MOD_ID, new ItemCrushingHammer("Steel Crushing Hammer", itemNum++, ToolMaterial.steel).setKey("steel_crushing_hammer"), "steel_hammer.png");


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
        EntityHelper.Core.createTileEntity(TileEntityHeater.class, "heater.tile");
    }

    @Override
    public void beforeGameStart() {

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

    @Override
    public void onRecipesReady() {
        RecipeBuilder.Shapeless(MOD_ID)
                .addInput(Item.clay)
                .addInput(Item.dustSugar)
                .addInput(Item.dustGlowstone)
                .create("potato", new ItemStack(itemPotato));

        RecipeBuilder.Shapeless(MOD_ID)
                .addInput(blockPotato)
                .create("potato", new ItemStack(itemPotato, 9));

        RecipeBuilder.Shapeless(MOD_ID)
                .addInput(itemWireSpool)
                .create("wire spool", new ItemStack(itemWireSpool));

        RecipeBuilder.Shapeless(MOD_ID)
                .addInput(Item.ingotIron)
                .addInput(Item.dustRedstone)
                .addInput(Item.dustRedstone)
                .create("redstone iron mix", new ItemStack(itemRedstoneIronMix));

        RecipeBuilder.Shaped(MOD_ID, "AAA", "AAA", "AAA")
                .addInput('A', itemPotato)
                .create("potato", new ItemStack(blockPotato));

        RecipeBuilder.Shaped(MOD_ID, "   ", "ABA", "   ")
                .addInput('A', Item.ingotIron)
                .addInput('B', Block.glass)
                .create("pipe", new ItemStack(blockPipe, 16));

        RecipeBuilder.Shaped(MOD_ID, "   ", "ABC", "   ")
                .addInput('A', Item.ingotIron)
                .addInput('B', Block.glass)
                .addInput('C', Item.ingotGold)
                .create("directional pipe", new ItemStack(blockDirectionalPipe, 16));

        RecipeBuilder.Shaped(MOD_ID, " A ", "AA ", "  A")
                .addInput('A', Item.ingotIron)
                .create("wrench", new ItemStack(itemWrench));

        RecipeBuilder.Shaped(MOD_ID, "ABA", "BCB", "ABA")
                .addInput('A', "minecraft:planks")
                .addInput('B', Item.dustRedstone)
                .addInput('C', Block.mesh)
                .create("filter", new ItemStack(blockFilter));

        RecipeBuilder.Shaped(MOD_ID, "AAA", "CBC", "CCC")
                .addInput('A', Item.leather)
                .addInput('B', Item.dustRedstone)
                .addInput('C', Item.wheat)
                .create("auto basket", new ItemStack(itemAutoBasket));

        RecipeBuilder.Shaped(MOD_ID,"ABA", "ECF", "ADA")
                .addInput('A', "minecraft:cobblestones")
                .addInput('B', Block.obsidian)
                .addInput('C', toolDiamondHammer)
                .addInput('D', Block.pistonBaseSticky)
                .addInput('E', blockPipe)
                .addInput('F', Item.dustRedstone)
                .create("block crusher", new ItemStack(blockBlockCrusher));

        RecipeBuilder.Shaped(MOD_ID, "ADA", "ACA", "ABA")
                .addInput('A', "minecraft:cobblestones")
                .addInput('B', blockPipe)
                .addInput('C', Item.dustRedstone)
                .addInput('D', Block.pistonBase)
                .create("block placer", new ItemStack(blockBlockPlacer));

        RecipeBuilder.Shaped(MOD_ID, "AAA", "BCD", "AEA")
                .addInput('A', Item.ingotIron)
                .addInput('B', Item.toolAxeDiamond)
                .addInput('C', blockIronMachineBlock)
                .addInput('D', blockPipe)
                .addInput('E', Item.dustRedstone)
                .create("tree chopper", new ItemStack(blockTreeChopper));

        RecipeBuilder.Shaped(MOD_ID, "ADA", "BCF", "AEA")
                .addInput('A', Item.ingotSteel)
                .addInput('B', Item.toolPickaxeDiamond)
                .addInput('C', blockSteelMachineBlock)
                .addInput('D', blockPipe)
                .addInput('E', Item.dustRedstone)
                .addInput('F', Item.toolShovelDiamond)
                .create("mining drill", new ItemStack(blockMiningDrill));

        RecipeBuilder.Shaped(MOD_ID, " A ", "A A", " A ")
                .addInput('A', Item.ingotIron)
                .create("iron gear", new ItemStack(itemIronGear));

        RecipeBuilder.Shaped(MOD_ID, " A ", "A A", " A ")
                .addInput('A', Item.ingotSteel)
                .create("steel gear", new ItemStack(itemSteelGear));

        RecipeBuilder.Shaped(MOD_ID, "AAA", "CBC", "AAA")
                .addInput('A', Item.ingotIron)
                .addInput('B', itemIronGear)
                .addInput('C', itemRedstoneAlloy)
                .create("iron machine block", new ItemStack(blockIronMachineBlock));

        RecipeBuilder.Shaped(MOD_ID, "AAA", "CBC", "AAA")
                .addInput('A', Item.ingotSteel)
                .addInput('B', itemSteelGear)
                .addInput('C', itemRedstoneAlloy)
                .create("steel machine block", new ItemStack(blockSteelMachineBlock));

        RecipeBuilder.Shaped(MOD_ID, " A ", "ABA", " A ")
                .addInput('A', itemRedstoneAlloy)
                .addInput('B', Item.stick)
                .create("wire spool", new ItemStack(itemWireSpool, 4));

        RecipeBuilder.Shaped(MOD_ID, " A ", "BAB", "BAB")
                .addInput('A', Item.ingotIron)
                .addInput('B', Item.brickClay)
                .create("energy connector", new ItemStack(itemEnergyConnector, 4));

        RecipeBuilder.Shaped(MOD_ID, "RRR", "ICI", "IGI")
                .addInput('R', Item.dustRedstone)
                .addInput('I', Item.ingotIron)
                .addInput('C', Block.workbench)
                .addInput('G', itemIronGear)
                .create("crafter", new ItemStack(blockAutoCrafter));

        RecipeBuilder.Shaped(MOD_ID, "III", "I I", "IFI")
                .addInput('I', Item.ingotIron)
                .addInput('F', Block.furnaceStoneIdle)
                .create("furnace burner", new ItemStack(blockFurnaceBurner));

        RecipeBuilder.Shaped(MOD_ID, "IPI", " M ", "IPI")
                .addInput('I', Item.ingotIron)
                .addInput('P', Block.pistonBase)
                .addInput('M', blockIronMachineBlock)
                .create("lv stirling engine", new ItemStack(blockStirlingEngine));

        RecipeBuilder.Shaped(MOD_ID, "IPI", " M ", "IPI")
                .addInput('I', Item.ingotGold)
                .addInput('P', Block.pistonBase)
                .addInput('M', blockSteelMachineBlock)
                .create("mv stirling engine", new ItemStack(blockStirlingEngineMV));

        RecipeBuilder.Shaped(MOD_ID, "WWW", "W W", "WWW")
                .addInput('W', itemWireSpool)
                .create("coil", new ItemStack(blockCoil));

        RecipeBuilder.Shaped(MOD_ID, "IAI", "ACA", "IMI")
                .addInput('I', Item.ingotIron)
                .addInput('A', itemRedstoneAlloy)
                .addInput('C', blockCoil)
                .addInput('M', blockIronMachineBlock)
                .create("heater", new ItemStack(blockHeater));

        RecipeBuilder.Shaped(MOD_ID, "SAS", "AOA", "SAS")
                .addInput('S', Item.ingotIron)
                .addInput('A', Block.blockOlivine)
                .addInput('O', Block.blockRedstone)
                .create("lv capacitor", new ItemStack(blockCapacitorLv));

        RecipeBuilder.Shaped(MOD_ID, "III", "I)I", "IRI")
                .addInput('I', Item.ingotIron)
                .addInput(')', Item.toolBow)
                .addInput('R', Item.dustRedstone)
                .create("advanced dispenser", new ItemStack(blockAdvancedDispenser));

        RecipeBuilder.Shaped(MOD_ID, "MMM", "MS ", " S ")
                .addInput('M', "minecraft:planks")
                .addInput('S', Item.stick)
                .create("wood crushing hammer", new ItemStack(toolWoodHammer));

        RecipeBuilder.Shaped(MOD_ID, "MMM", "MS ", " S ")
                .addInput('M', "minecraft:cobblestones")
                .addInput('S', Item.stick)
                .create("stone crushing hammer", new ItemStack(toolStoneHammer));

        RecipeBuilder.Shaped(MOD_ID, "MMM", "MS ", " S ")
                .addInput('M', Item.ingotIron)
                .addInput('S', Item.stick)
                .create("iron crushing hammer", new ItemStack(toolIronHammer));

        RecipeBuilder.Shaped(MOD_ID, "MMM", "MS ", " S ")
                .addInput('M', Item.diamond)
                .addInput('S', Item.stick)
                .create("diamond crushing hammer", new ItemStack(toolDiamondHammer));

        RecipeBuilder.Shaped(MOD_ID, "MMM", "MS ", " S ")
                .addInput('M', Item.ingotSteel)
                .addInput('S', Item.stick)
                .create("steel crushing hammer", new ItemStack(toolSteelHammer));

        RecipeBuilder.Furnace(MOD_ID)
                .setInput(itemRedstoneIronMix)
                .create("redstone alloy", new ItemStack(itemRedstoneAlloy));

        // === Recipes for skyblock ===
        RecipeBuilder.Shaped(MOD_ID, "AAA", "AAA", "AAA")
                .addInput('A', Item.chainlink)
                .create("Iron Mesh", new ItemStack(Block.mesh, 1));

        RecipeBuilder.Shapeless(MOD_ID)
                .addInput(Block.sand)
                .addInput(Item.bone)
                .create("Soul Sand", new ItemStack(Block.soulsand));

        Registries.ITEM_GROUPS.register(MOD_ID+":saplings", Arrays.asList(new ItemStack[]{
                new ItemStack(Block.saplingOak),
                new ItemStack(Block.saplingBirch),
                new ItemStack(Block.saplingCacao),
                new ItemStack(Block.saplingCherry),
                new ItemStack(Block.saplingEucalyptus),
                new ItemStack(Block.saplingOakRetro),
                new ItemStack(Block.saplingPine),
        }));

        RecipeBuilder.Shaped(MOD_ID, "PS", "SP")
                .addInput('S', MOD_ID+":saplings")
                .addInput('P', Item.ammoPebble)
                .create("Dirt", new ItemStack(Block.dirt, 2));

        RecipeBuilder.Trommel(MOD_ID)
                .setInput(blockDust)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Item.diamond), 1), 1)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Item.dustRedstone), 4, 8), 10)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Item.clay), 1, 2), 79)
                .create("dust");

        RecipeBuilder.Trommel(MOD_ID)
                .setInput("minecraft:leaves")
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingPine), 1), 1.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingBirch), 1), 1.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingCacao), 1), 0.1)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingCherry), 1), 0.1)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingOak), 1), 1.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingEucalyptus), 1), 1.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingOakRetro), 1), 0.01)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Block.saplingShrub), 1), 1.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Item.seedsWheat), 1), 100.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Item.seedsPumpkin), 1), 10.0)
                .addEntry(new WeightedRandomLootObject(new ItemStack(Item.sugarcane), 1), 10.0)
                .create("leaves");
    }

    public static RecipeNamespace POTATO_LOGISTICS = new RecipeNamespace();

    public static RecipeGroup<RecipeEntryCrafting<?,?>> WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));
    public static RecipeGroup<RecipeEntryFurnace> FURNACE = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.furnaceStoneActive)));
    public static RecipeGroup<RecipeEntryFurnace> TROMMEL = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.trommelActive)));
    @Override
    public void initNamespaces() {
        LOGGER.info("init namespaces");
        POTATO_LOGISTICS = new RecipeNamespace();
        WORKBENCH = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.workbench)));
        FURNACE = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.furnaceStoneActive)));
        TROMMEL = new RecipeGroup<>(new RecipeSymbol(new ItemStack(Block.trommelActive)));

        POTATO_LOGISTICS.register("workbench", WORKBENCH);
        POTATO_LOGISTICS.register("furnace", FURNACE);
        POTATO_LOGISTICS.register("trommel", TROMMEL);
        Registries.RECIPES.register(MOD_ID, POTATO_LOGISTICS);
    }
}

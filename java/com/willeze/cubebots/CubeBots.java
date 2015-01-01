package com.willeze.cubebots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

import com.willeze.cubebots.block.BlockChestMarker;
import com.willeze.cubebots.entity.CubeBot;
import com.willeze.cubebots.entity.CubeBotCollect;
import com.willeze.cubebots.entity.CubeBotEnum;
import com.willeze.cubebots.entity.CubeBotLumber;
import com.willeze.cubebots.entity.CubeBotMiner;
import com.willeze.cubebots.entity.CubeBotTeleport;
import com.willeze.cubebots.item.ItemCubeBotSpawner;
import com.willeze.cubebots.item.ItemCubeCall;
import com.willeze.cubebots.item.ItemInstantCubeCall;
import com.willeze.cubebots.tile.BlockMarker;
import com.willeze.cubebots.tile.TileEntityMarker;
import com.willeze.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = CubeBots.MODID, name = CubeBots.MODID, version = CubeBots.VERSION)
public class CubeBots {

	@Instance(CubeBots.MODID)
	public static CubeBots instance;

	@SidedProxy(clientSide = "com.willeze.proxy.ClientProxy", serverSide = "com.willeze.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static final String MODID = "cubebots";
	public static final String VERSION = "0.1.0";

	public static CreativeTabs creativeTab;
	
	// test
	public static Block blockMarkerChest, blockMarkerPig, blockMarkerChicken, blockMarkerOre, blockMarkerWolf, blockMarkerOcelot;
	public static Item itemMarker;

	public static Block blockChestMarker, blockOreMarker;
	public static Item itemCubeBotSpawn, itemCubeBotCollectSpawn, itemCubeBotLumberSpawn, itemCubeBotMinerSpawn, itemCubeBotTeleportSpawn;
	public static Item itemCubeCall, itemInstantCubeCall, itemCubePiece, itemCubeBotCore, itemLifeCore, itemInfinityCore;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {		
		creativeTab = new CreativeTab(CubeBots.MODID);
		
		// testing
		itemMarker = new Item().setUnlocalizedName("marker").setTextureName(CubeBots.MODID + ":marker").setFull3D().setCreativeTab(CubeBots.creativeTab);
		GameRegistry.registerItem(itemMarker, itemMarker.getUnlocalizedName());
		
		blockMarkerChest = new BlockMarker("markerChest", Blocks.chest, null, 0.75F).setBlockTextureName("markerChest").setBlockName("markerChest").setCreativeTab(CubeBots.creativeTab);
		blockMarkerPig = new BlockMarker("markerPig", null, "Pig", 1F).setBlockTextureName("markerPig").setBlockName("markerPig").setCreativeTab(CubeBots.creativeTab);
		blockMarkerChicken = new BlockMarker("markerChicken", null, "Chicken", 1F).setBlockTextureName("markerChicken").setBlockName("markerChicken").setCreativeTab(CubeBots.creativeTab);
		blockMarkerOre = new BlockMarker("markerOre", Blocks.coal_ore, null, 0.75F).setBlockTextureName("markerOre").setBlockName("markerOre").setCreativeTab(CubeBots.creativeTab);
		blockMarkerWolf = new BlockMarker("markerWolf", null, "Wolf", 1F).setBlockTextureName("markerWolf").setBlockName("markerWolf").setCreativeTab(CubeBots.creativeTab);
		blockMarkerOcelot = new BlockMarker("markerOcelot", null, "Ocelot", 1F).setBlockTextureName("markerOcelot").setBlockName("markerOcelot").setCreativeTab(CubeBots.creativeTab);

		GameRegistry.registerTileEntity(TileEntityMarker.class, "marker");
		
		GameRegistry.registerBlock(blockMarkerChest, blockMarkerChest.getUnlocalizedName());
		GameRegistry.registerBlock(blockMarkerPig, blockMarkerPig.getUnlocalizedName());
		GameRegistry.registerBlock(blockMarkerChicken, blockMarkerChicken.getUnlocalizedName());
		GameRegistry.registerBlock(blockMarkerOre, blockMarkerOre.getUnlocalizedName());
		GameRegistry.registerBlock(blockMarkerWolf, blockMarkerWolf.getUnlocalizedName());
		GameRegistry.registerBlock(blockMarkerOcelot, blockMarkerOcelot.getUnlocalizedName());

		// blocks
		blockChestMarker = new BlockChestMarker().setBlockName("blockChestMarker");
		blockOreMarker = new BlockChestMarker().setBlockName("blockOreMarker");
		
		// register blocks
		GameRegistry.registerBlock(blockChestMarker, blockChestMarker.getUnlocalizedName());
		GameRegistry.registerBlock(blockOreMarker, blockOreMarker.getUnlocalizedName());

		// items
		itemCubeCall = new ItemCubeCall().setUnlocalizedName("itemCubeCall").setTextureName(CubeBots.MODID + ":cubeCall").setCreativeTab(CubeBots.creativeTab);
		itemInstantCubeCall = new ItemInstantCubeCall().setUnlocalizedName("itemInstantCubeCall").setTextureName(CubeBots.MODID + ":instantCubeCall").setCreativeTab(CubeBots.creativeTab);
		itemCubePiece = new Item().setUnlocalizedName("itemCubePiece").setTextureName(CubeBots.MODID + ":cubePiece").setCreativeTab(CubeBots.creativeTab);
		itemCubeBotCore = new Item().setUnlocalizedName("itemCubeBotCore").setTextureName(CubeBots.MODID + ":cubeBotCore").setCreativeTab(CubeBots.creativeTab);
		itemLifeCore = new Item().setUnlocalizedName("itemLifeCore").setTextureName(CubeBots.MODID + ":lifeCore").setCreativeTab(CubeBots.creativeTab);
		itemInfinityCore = new Item().setUnlocalizedName("itemInfinityCore").setTextureName(CubeBots.MODID + ":infinityCore").setCreativeTab(CubeBots.creativeTab);
		itemCubeBotSpawn = new ItemCubeBotSpawner(CubeBotEnum.NORMAL).setUnlocalizedName("itemCubeBotSpawn").setTextureName(CubeBots.MODID + ":cubeBotSpawn").setCreativeTab(CubeBots.creativeTab);
		itemCubeBotCollectSpawn = new ItemCubeBotSpawner(CubeBotEnum.COLLECT).setUnlocalizedName("itemCubeBotCollectSpawn").setTextureName(CubeBots.MODID + ":cubeBotCollectSpawn").setCreativeTab(CubeBots.creativeTab);
		itemCubeBotLumberSpawn = new ItemCubeBotSpawner(CubeBotEnum.LUMBER).setUnlocalizedName("itemCubeBotLumberSpawn").setTextureName(CubeBots.MODID + ":cubeBotLumberSpawn").setCreativeTab(CubeBots.creativeTab);
		itemCubeBotMinerSpawn = new ItemCubeBotSpawner(CubeBotEnum.MINER).setUnlocalizedName("itemCubeBotMinerSpawn").setTextureName(CubeBots.MODID + ":cubeBotMinerSpawn").setCreativeTab(CubeBots.creativeTab);
		itemCubeBotTeleportSpawn = new ItemCubeBotSpawner(CubeBotEnum.TELEPORT).setUnlocalizedName("itemCubeBotTeleportSpawn").setTextureName(CubeBots.MODID + ":cubeBotTeleportSpawn").setCreativeTab(CubeBots.creativeTab);

		// register items
		GameRegistry.registerItem(itemCubeCall, itemCubeCall.getUnlocalizedName());
		GameRegistry.registerItem(itemInstantCubeCall, itemInstantCubeCall.getUnlocalizedName());
		GameRegistry.registerItem(itemCubePiece, itemCubePiece.getUnlocalizedName());
		GameRegistry.registerItem(itemCubeBotCore, itemCubeBotCore.getUnlocalizedName());
		GameRegistry.registerItem(itemLifeCore, itemLifeCore.getUnlocalizedName());
		GameRegistry.registerItem(itemInfinityCore, itemInfinityCore.getUnlocalizedName());
		GameRegistry.registerItem(itemCubeBotSpawn, itemCubeBotSpawn.getUnlocalizedName());
		GameRegistry.registerItem(itemCubeBotCollectSpawn, itemCubeBotCollectSpawn.getUnlocalizedName());
		GameRegistry.registerItem(itemCubeBotLumberSpawn, itemCubeBotLumberSpawn.getUnlocalizedName());
		GameRegistry.registerItem(itemCubeBotMinerSpawn, itemCubeBotMinerSpawn.getUnlocalizedName());
		GameRegistry.registerItem(itemCubeBotTeleportSpawn, itemCubeBotTeleportSpawn.getUnlocalizedName());

		proxy.registerRenderers();
		
		// register entities
		registerCubeBot(CubeBot.class, "entityCubeBot");
		registerCubeBot(CubeBotCollect.class, "entityCubeBotCollect");
		registerCubeBot(CubeBotLumber.class, "entityCubeBotLumber");
		registerCubeBot(CubeBotMiner.class, "entityCubeBotMiner");
		registerCubeBot(CubeBotTeleport.class, "entityCubeBotTeleport");
		
		// recipes - items
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemCubeCall), new Object[] {
			"A", "B", "C", 'A', CubeBots.itemCubePiece, 'B', Items.redstone, 'C', Items.iron_ingot
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemInstantCubeCall), new Object[] {
			"A", "B", 'A', Items.ender_eye, 'B', CubeBots.itemCubeCall
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemInfinityCore), new Object[] {
			"AAA", "ABA", "AAA", 'A', CubeBots.itemLifeCore, 'B', Items.blaze_rod
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemCubeBotCore, 4), new Object[] {
			"AA", "AA", 'A', CubeBots.itemLifeCore
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemLifeCore, 4), new Object[] {
			" A ", "ABA", " A ", 'A', CubeBots.itemCubeBotCore, 'B', Items.lava_bucket
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemLifeCore, 4), new Object[] {
			"ABA", "ACA", "ADA", 'A', Items.iron_ingot, 'B', CubeBots.itemCubePiece, 'C', Items.diamond
		});
		
		// recipes - markers
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerChicken, 4), new Object[] {
			"A", "B", 'A', Items.feather, 'B', CubeBots.itemMarker
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerOre, 4), new Object[] {
			"A", "B", 'A', Items.coal, 'B', CubeBots.itemMarker
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerPig, 4), new Object[] {
			"A", "B", 'A', Items.porkchop, 'B', CubeBots.itemMarker
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerPig, 4), new Object[] {
			"A", "B", 'A', Items.cooked_porkchop, 'B', CubeBots.itemMarker
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerWolf, 4), new Object[] {
			"A", "B", 'A', Items.bone, 'B', CubeBots.itemMarker
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerOcelot, 4), new Object[] {
			"A", "B", 'A', Items.fish, 'B', CubeBots.itemMarker
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.blockMarkerChest, 4), new Object[] {
			"A", "B", 'A', Blocks.chest, 'B', CubeBots.itemMarker
		});
		
		// recipes - bots
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemCubeBotSpawn), new Object[] {
			"ABA", "ACA", " D ", 'A', Items.iron_ingot, 'B', CubeBots.itemCubeBotCore, 'C', Items.lava_bucket, 'D', Items.redstone
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemCubeBotCollectSpawn), new Object[] {
			"A", "B", "C", 'A', Blocks.chest, 'B', Items.redstone, 'C', CubeBots.itemCubeBotSpawn
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemCubeBotLumberSpawn), new Object[] {
			"A", "B", "C", 'A', Items.wooden_axe, 'B', Items.redstone, 'C', CubeBots.itemCubeBotSpawn
		});
		GameRegistry.addShapedRecipe(new ItemStack(CubeBots.itemCubeBotMinerSpawn), new Object[] {
			"A", "B", "C", 'A', Items.wooden_pickaxe, 'B', Items.redstone, 'C', CubeBots.itemCubeBotSpawn
		});
	}

	public static void registerCubeBot(Class entityClass, String name) {
		int entityID = EntityRegistry.findGlobalUniqueEntityId();
		long seed = name.hashCode();
		Random rand = new Random(seed);
		int primaryColor = rand.nextInt() * 16777215;
		int secondaryColor = rand.nextInt() * 16777215;

		EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
		EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 3, false);
		EntityList.entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, primaryColor, secondaryColor));
	}

}

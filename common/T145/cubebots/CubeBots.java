package T145.cubebots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import T145.cubebots.block.BlockMarker;
import T145.cubebots.entity.CubeBadZombie;
import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.CubeBotArcher;
import T145.cubebots.entity.CubeBotBreeder;
import T145.cubebots.entity.CubeBotButcher;
import T145.cubebots.entity.CubeBotChickBring;
import T145.cubebots.entity.CubeBotCollector;
import T145.cubebots.entity.CubeBotFarmer;
import T145.cubebots.entity.CubeBotFighter;
import T145.cubebots.entity.CubeBotFixer;
import T145.cubebots.entity.CubeBotGrasser;
import T145.cubebots.entity.CubeBotLumber;
import T145.cubebots.entity.CubeBotMilker;
import T145.cubebots.entity.CubeBotMiner;
import T145.cubebots.entity.CubeBotOreFinder;
import T145.cubebots.entity.CubeBotSiren;
import T145.cubebots.entity.CubeBotSmithy;
import T145.cubebots.entity.CubeBotTamer;
import T145.cubebots.entity.CubeBotTeleporter;
import T145.cubebots.entity.EnumCubeBotType;
import T145.cubebots.item.ItemCubeBotSpawner;
import T145.cubebots.item.ItemCubeCall;
import T145.cubebots.lib.BehaviorDispenseCB;
import T145.cubebots.lib.Configuration;
import T145.cubebots.lib.CreatingHandler;
import T145.cubebots.lib.GuiHandler;
import T145.cubebots.network.CommonProxy;
import T145.cubebots.network.packet.PacketHandlerClient;
import T145.cubebots.network.packet.PacketHandlerServer;
import T145.cubebots.network.packet.TickHandlerClient;
import T145.cubebots.network.packet.TickHandlerServer;
import T145.cubebots.tileentity.TileEntityMarker;
import T145.cubebots.world.WorldGenerator;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "CubeBots", name = "CubeBots", version = "1.4.3")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = "CubeBots", packetHandler = PacketHandlerClient.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = "CubeBots", packetHandler = PacketHandlerServer.class))
public class CubeBots {
	static final String modName = "CubeBots";

	@Instance(modName)
	public static CubeBots instance;

	@SidedProxy(clientSide = "T145.cubebots.network.ClientProxy", serverSide = "T145.cubebots.network.CommonProxy")
	public static CommonProxy proxy;

	public static Configuration config = new Configuration();
	public static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config.setConfigHeader(modName);
		config.loadUsing(event.getSuggestedConfigurationFile());
		logger = Logger.getLogger(modName);
		logger.setParent(FMLLog.getLogger());
		initializeStuffs();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TickRegistry.registerTickHandler(new TickHandlerServer(), Side.SERVER);
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);

		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());

		GameRegistry.registerWorldGenerator(new WorldGenerator());
		GameRegistry.registerCraftingHandler(new CreatingHandler());
		GameRegistry.registerFuelHandler(new CreatingHandler());

		BlockDispenser.dispenseBehaviorRegistry.putObject(cubeBotCore, new BehaviorDispenseCB());
		BlockDispenser.dispenseBehaviorRegistry.putObject(lifeCore, new BehaviorDispenseCB());
		BlockDispenser.dispenseBehaviorRegistry.putObject(infiniteLifeCore, new BehaviorDispenseCB());

		proxy.registerRenderers();
		registerEntities();
		registerStuffs();
		addItemRecipes();
		addBlockRecipes();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		config.save();
	}

	public static int monsterSpawnRate = config.getInt("Monsters spawn rate", 3, "The higher the more chance");

	public void registerEntities() {
		registerCubeBot(CubeBot.class, "CubeBot", 1);
		registerCubeBot(CubeBotCollector.class, "CubeBotCollector", 2);
		registerCubeBot(CubeBotLumber.class, "CubeBotLumber", 3);
		registerCubeBot(CubeBotFighter.class, "CubeBotFighter", 4);
		registerCubeBot(CubeBotBreeder.class, "CubeBotBreeder", 5);
		registerCubeBot(CubeBotFixer.class, "CubeBotFixer", 6);
		registerCubeBot(CubeBotFarmer.class, "CubeBotFarmer", 7);
		registerCubeBot(CubeBotArcher.class, "CubeBotArcher", 8);
		registerCubeBot(CubeBotMilker.class, "CubeBotMilker", 9);
		registerCubeBot(CubeBotButcher.class, "CubeBotButcher", 10);
		registerCubeBot(CubeBotSmithy.class, "CubeBotSmithy", 11);
		registerCubeBot(CubeBotChickBring.class, "CubeBotChickBring", 12);
		registerCubeBot(CubeBotOreFinder.class, "CubeBotOreFinder", 13);
		registerCubeBot(CubeBotMiner.class, "CubeBotMiner", 14);
		registerCubeBot(CubeBotTeleporter.class, "CubeBotTeleporter", 15);
		registerCubeBot(CubeBotSiren.class, "CubeBotSiren", 16);
		registerCubeBot(CubeBotTamer.class, "CubeBotTamer", 17);
		registerCubeBot(CubeBotGrasser.class, "CubeBotGrasser", 18);
		// registerCubeBot(NetherBot.class, "CubeBotNether", 32);
		registerCubeBot(CubeBadZombie.class, "CubeBadZombie", 33);

		BiomeGenBase[] butHell = allBiomesExcept(BiomeGenBase.hell);
		if (monsterSpawnRate > 0) {
			if (butHell != null && butHell.length > 0) {
				EntityRegistry.addSpawn(CubeBadZombie.class, monsterSpawnRate, 1, 2, EnumCreatureType.monster, butHell);
			} else {
				EntityRegistry.addSpawn(CubeBadZombie.class, monsterSpawnRate, 1, 2, EnumCreatureType.monster);
			}
		}
		// EntityRegistry.addSpawn(NetherBot.class, 50, 4, 8,
		// EnumCreatureType.monster, BiomeGenBase.hell);
	}

	public static int updateFrequency = config.getInt("Bots update frequency", 1, "The lower the faster update, 0-5, increase the value if you experience great delay.");

	public void registerCubeBot(Class<? extends Entity> entityClass, String entityName, int id) {
		registerModEntity(entityClass, entityName, id, this, 64, updateFrequency, true);
	}

	public void registerModEntity(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
		LanguageRegistry.instance().addStringLocalization("entity.CubeBots." + entityName + ".name", entityName);
	}

	public BiomeGenBase[] allBiomesExcept(BiomeGenBase... bases) {
		List<BiomeGenBase> l1 = new ArrayList();
		l1.addAll(Arrays.asList(BiomeGenBase.biomeList));
		l1.removeAll(Collections.singleton(null));
		l1.removeAll(Arrays.asList(bases));
		BiomeGenBase[] bgb = new BiomeGenBase[l1.size()];
		bgb = l1.toArray(new BiomeGenBase[0]);
		return bgb;
	}

	public static CreativeTabs cubeBotTab;
	public static Item marker;
	public static Item cubePiece, cubeBotCore, lifeCore, infiniteLifeCore, cubeCall, instantCubeCall;
	// public static Item netherPiece;
	public static Item cubeBotSpawn, cubeBotCollectorSpawn, cubeBotLumberSpawn, cubeBotFighterSpawn, cubeBotBreederSpawn, cubeBotFixerSpawn;
	public static Item cubeBotFarmerSpawn, cubeBotArcherSpawn, cubeBotMilkerSpawn, cubeBotButcherSpawn, cubeBotSmithySpawn, cubeBotChickBringSpawn;
	public static Item cubeBotOreFinderSpawn, cubeBotMinerSpawn, cubeBotTeleporterSpawn, cubeBotSirenSpawn, cubeBotTamerSpawn, cubeBotGrasserSpawn;
	// public static Item netherBotSpawn;
	public static Block markerChest, markerPig, markerChicken, markerOre, markerWolf, markerCat;

	public void initializeStuffs() {
		CreativeTabs cubeBotTab = new CreativeTabs("cubebots") {
			public ItemStack getIconItemStack() {
				return new ItemStack(CubeBots.cubeBotSpawn);
			}
		};
		
		marker = new Item(config.getInt("Marker id", 21210)).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":marker");
		cubePiece = new Item(config.getInt("Cube Piece id", 21211)).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubePiece");
		cubeBotCore = new Item(config.getInt("CubeBot Power Core id", 21212)).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotCore");
		lifeCore = new Item(config.getInt("Life Core id", 21213)).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":lifeCore");
		infiniteLifeCore = new Item(config.getInt("Infinite Life Core id", 21214)).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":infiniteLifeCore");
		cubeCall = new ItemCubeCall(config.getInt("CubeCall id", 21215), false).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeCall");
		instantCubeCall = new ItemCubeCall(config.getInt("Instant CubeCall id", 21216), true).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":instantCubeCall");
		// netherPiece = new Item(config.getInt("Nether Piece id", 21217)).setFull3D().setCreativeTab(cubeBotTab).setUnlocalizedName(modName+":netherPiece");

		cubeBotSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Spawner id", 21220), EnumCubeBotType.NORMAL).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotSpawn");
		cubeBotCollectorSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Collector Spawner id", 21221), EnumCubeBotType.COLLECTOR).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotCollectorSpawn");
		cubeBotLumberSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Lumber Spawner id", 21222), EnumCubeBotType.LUMBER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotLumberSpawn");
		cubeBotFighterSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Fighter Spawner id", 21223), EnumCubeBotType.FIGHTER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotFighterSpawn");
		cubeBotBreederSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Breeder Spawner id", 21224), EnumCubeBotType.BREEDER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotBreederSpawn");
		cubeBotFixerSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Fixer Spawner id", 21225), EnumCubeBotType.FIXER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotFixerSpawn");
		cubeBotFarmerSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Farmer Spawner id", 21226), EnumCubeBotType.FARMER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotFarmerSpawn");
		cubeBotArcherSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Archer Spawner id", 21227), EnumCubeBotType.ARCHER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotArcherSpawn");
		cubeBotMilkerSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Milker Spawner id", 21228), EnumCubeBotType.MILKER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotMilkerSpawn");
		cubeBotButcherSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Butcher Spawner id", 21229), EnumCubeBotType.BUTCHER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotButcherSpawn");
		cubeBotSmithySpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Smithy Spawner id", 21230), EnumCubeBotType.SMITHY).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotSmithySpawn");
		cubeBotChickBringSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Chicken Bringer Spawner id", 21231), EnumCubeBotType.CHICKBRING).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotChickBringSpawn");
		cubeBotOreFinderSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Ore Finder Spawner id", 21232), EnumCubeBotType.OREFINDER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotOreFinderSpawn");
		cubeBotMinerSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Miner Spawner id", 21233), EnumCubeBotType.MINER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotMinerSpawn");
		cubeBotTeleporterSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Teleporter Spawner id", 21234),EnumCubeBotType.TELEPORTER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotTeleporterSpawn");
		cubeBotSirenSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Siren Spawner id", 21235), EnumCubeBotType.SIREN).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotSirenSpawn");
		cubeBotTamerSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Tamer Spawner id", 21236), EnumCubeBotType.TAMER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotTamerSpawn");
		cubeBotGrasserSpawn = new ItemCubeBotSpawner(config.getInt("CubeBot Grasser Spawner id", 21237), EnumCubeBotType.GRASSER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":cubeBotGrasserSpawn");
		// netherBotSpawn = new ItemCubeBotSpawner(config.getInt("NetherBot Spawner id", 21250),
		// EnumCubeBotType.NETHER).setCreativeTab(cubeBotTab).setUnlocalizedName(modName+":netherBotSpawn");

		markerChest = new BlockMarker(config.getInt("Chest Marker id", 2777), Block.chest, null, 0.75F).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":markerChest");
		markerPig = new BlockMarker(config.getInt("Pig Marker id", 2778), null, "Pig", 1F).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":markerPig");
		markerChicken = new BlockMarker(config.getInt("Chicken Marker id", 2779), null, "Chicken", 1F).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":markerChicken");
		markerOre = new BlockMarker(config.getInt("Ore Marker id", 2780), Block.oreCoal, null, 0.75F).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":markerOre");
		markerWolf = new BlockMarker(config.getInt("Wolf Marker id", 2781), null, "Wolf", 1F).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":markerWolf");
		markerCat = new BlockMarker(config.getInt("Ocelot Marker id", 2782), null, "Ozelot", 1F).setCreativeTab(cubeBotTab).setUnlocalizedName(modName + ":markerCat");
	}

	public void registerStuffs() {
		LanguageRegistry.instance().addStringLocalization("itemGroup.cubebots", "CubeBots");

		LanguageRegistry.addName(marker, "Marker");
		LanguageRegistry.addName(cubePiece, "Cube Piece");
		LanguageRegistry.addName(cubeBotCore, "CubeBot Power Core");
		LanguageRegistry.addName(lifeCore, "Life Core");
		LanguageRegistry.addName(infiniteLifeCore, "Infinite Life Core");
		LanguageRegistry.addName(cubeCall, "CubeCall");
		LanguageRegistry.addName(instantCubeCall, "Instant CubeCall");
		// LanguageRegistry.addName(netherPiece, "Nether Piece");

		LanguageRegistry.addName(cubeBotSpawn, "CubeBot");
		LanguageRegistry.addName(cubeBotCollectorSpawn, "CubeBot Collector");
		LanguageRegistry.addName(cubeBotLumberSpawn, "CubeBot Lumber");
		LanguageRegistry.addName(cubeBotFighterSpawn, "CubeBot Fighter");
		LanguageRegistry.addName(cubeBotBreederSpawn, "CubeBot Breeder");
		LanguageRegistry.addName(cubeBotFixerSpawn, "CubeBot Fixer");
		LanguageRegistry.addName(cubeBotFarmerSpawn, "CubeBot Farmer");
		LanguageRegistry.addName(cubeBotArcherSpawn, "CubeBot Archer");
		LanguageRegistry.addName(cubeBotMilkerSpawn, "CubeBot Milker");
		LanguageRegistry.addName(cubeBotButcherSpawn, "CubeBot Butcher");
		LanguageRegistry.addName(cubeBotSmithySpawn, "CubeBot Smithy");
		LanguageRegistry.addName(cubeBotChickBringSpawn, "CubeBot Chicken Bringer");
		LanguageRegistry.addName(cubeBotOreFinderSpawn, "CubeBot Ore Finder");
		LanguageRegistry.addName(cubeBotMinerSpawn, "CubeBot Miner");
		LanguageRegistry.addName(cubeBotTeleporterSpawn, "CubeBot Teleporter");
		LanguageRegistry.addName(cubeBotSirenSpawn, "CubeBot Siren");
		LanguageRegistry.addName(cubeBotTamerSpawn, "CubeBot Tamer");
		LanguageRegistry.addName(cubeBotGrasserSpawn, "CubeBot Grasser");
		// LanguageRegistry.addName(netherBotSpawn, "NetherBot");

		GameRegistry.registerTileEntity(TileEntityMarker.class, "Cb Marker");
		GameRegistry.registerBlock(markerChest, "Cb Chest Marker");
		GameRegistry.registerBlock(markerPig, "Cb Pig Marker");
		GameRegistry.registerBlock(markerChicken, "Cb Chicken Marker");
		GameRegistry.registerBlock(markerOre, "Cb Ore Marker");
		GameRegistry.registerBlock(markerWolf, "Cb Wolf Marker");
		GameRegistry.registerBlock(markerCat, "Cb Ocelot Marker");
		LanguageRegistry.addName(markerChest, "Chest Marker");
		LanguageRegistry.addName(markerPig, "Pig Marker");
		LanguageRegistry.addName(markerChicken, "Chicken Marker");
		LanguageRegistry.addName(markerOre, "Ore Marker");
		LanguageRegistry.addName(markerWolf, "Wolf Marker");
		LanguageRegistry.addName(markerCat, "Ocelot Marker");
	}

	public void addItemRecipes() {
		GameRegistry.addRecipe(new ItemStack(marker), new Object[] { "P", "W", 'W', Block.cloth, 'P', Block.planks });
		GameRegistry.addRecipe(new ItemStack(cubeBotCore), new Object[] { "IRI", "IPI", "IDI", 'I', Item.ingotIron, 'R', Item.redstone, 'D', Item.diamond, 'P', cubePiece });
		GameRegistry.addRecipe(new ItemStack(lifeCore, 4), new Object[] { " C ", "CLC", " C ", 'C', cubeBotCore, 'L', Item.bucketLava });
		GameRegistry.addShapelessRecipe(new ItemStack(cubeBotCore, 4), new Object[] { lifeCore, lifeCore, lifeCore, lifeCore });
		GameRegistry.addRecipe(new ItemStack(infiniteLifeCore), new Object[] { "CCC", "CLC", "CCC", 'C', lifeCore, 'L', Item.blazeRod });
		GameRegistry.addRecipe(new ItemStack(cubeCall), new Object[] { "P", "R", "I", 'P', cubePiece, 'R', Item.redstone, 'I', Item.ingotIron });
		GameRegistry.addRecipe(new ItemStack(instantCubeCall), new Object[] { "E", "C", 'E', Item.eyeOfEnder, 'C', cubeCall });
		GameRegistry.addRecipe(new ItemStack(cubeBotSpawn), new Object[] { "ICI", "IHI", "SRS", 'I', Item.ingotIron, 'R', Item.redstone, 'C', cubeBotCore, 'H', Item.bucketLava });
		GameRegistry.addRecipe(new ItemStack(cubeBotCollectorSpawn), new Object[] { "S", "R", "C", 'S', Block.chest, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotLumberSpawn), new Object[] { "S", "R", "C", 'S', Item.axeWood, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotFighterSpawn), new Object[] { "S", "R", "C", 'S', Item.swordWood, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotBreederSpawn), new Object[] { "S", "R", "C", 'S', cubeBotCore, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotFixerSpawn), new Object[] { "SSS", " R ", " C ", 'S', cubePiece, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotFarmerSpawn), new Object[] { "B", "R", "C", 'B', Item.hoeWood, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotArcherSpawn), new Object[] { "B", "R", "C", 'B', Item.bow, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotMilkerSpawn), new Object[] { "B", "R", "C", 'B', Item.bucketMilk, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotButcherSpawn), new Object[] { "B", "R", "C", 'B', Item.porkRaw, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotButcherSpawn), new Object[] { "B", "R", "C", 'B', Item.porkCooked, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotSmithySpawn), new Object[] { "B", "R", "C", 'B', Block.anvil, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotChickBringSpawn), new Object[] { "B", "R", "C", 'B', Item.feather, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotOreFinderSpawn), new Object[] { "B", "R", "C", 'B', Item.compass, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotMinerSpawn), new Object[] { "B", "R", "C", 'B', Item.pickaxeWood, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotTeleporterSpawn), new Object[] { "B", "R", "C", 'B', Item.eyeOfEnder, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotSirenSpawn), new Object[] { "B", "R", "C", 'B', Item.rottenFlesh, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotTamerSpawn), new Object[] { "B", "R", "C", 'B', Item.bone, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotTamerSpawn), new Object[] { "B", "R", "C", 'B', Item.fishRaw, 'R', Item.redstone, 'C', cubeBotSpawn });
		GameRegistry.addRecipe(new ItemStack(cubeBotGrasserSpawn), new Object[] { "B", "R", "C", 'B', Item.shears, 'R', Item.redstone, 'C', cubeBotSpawn });
		// GameRegistry.addRecipe(new ItemStack(netherBotSpawn), new Object[] { "B", "C", 'B', netherPiece, 'C', cubeBotSpawn });
	}

	public void addBlockRecipes() {
		GameRegistry.addRecipe(new ItemStack(markerChest, 4), new Object[] { "C", "S", 'C', Block.chest, 'S', marker });
		GameRegistry.addRecipe(new ItemStack(markerPig, 4), new Object[] { "C", "S", 'C', Item.porkCooked, 'S', marker });
		GameRegistry.addRecipe(new ItemStack(markerPig, 4), new Object[] { "C", "S", 'C', Item.porkRaw, 'S', marker });
		GameRegistry.addRecipe(new ItemStack(markerChicken, 4), new Object[] { "C", "S", 'C', Item.feather, 'S', marker });
		GameRegistry.addRecipe(new ItemStack(markerOre, 4), new Object[] { "C", "S", 'C', Item.coal, 'S', marker });
		GameRegistry.addRecipe(new ItemStack(markerWolf, 4), new Object[] { "C", "S", 'C', Item.bone, 'S', marker });
		GameRegistry.addRecipe(new ItemStack(markerCat, 4), new Object[] { "C", "S", 'C', Item.fishRaw, 'S', marker });
	}
}
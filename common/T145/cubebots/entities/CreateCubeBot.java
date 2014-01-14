package T145.cubebots.entities;

import java.util.Random;

import net.minecraft.world.World;

public class CreateCubeBot {
	public static void cubeBotSpawnInChunk(World world, Random rnd, int cx, int cz) {
		int posX = cx * 16;
		int posZ = cz * 16;
		for (int x = posX; x < posX + 16; x += rnd.nextInt(5) + 3) {
			for (int z = posZ; z < posZ + 16; z += rnd.nextInt(5) + 3) {
				for (int y = 4; y < 24; y += rnd.nextInt(4) + 2) {
					if (rnd.nextInt(100) <= 25) {
						CubeBot cb = createCubeBot(world, EnumCubeBotType.NORMAL);
						cb.setPosition(x + 0.5, y + 1, z + 0.5);
						if (cb.getCanSpawnHere()) world.spawnEntityInWorld(cb);
					}
				}
			}
		}
	}

	public static CubeBot createCubeBot(World w, EnumCubeBotType type) {
		try {
			return createCubeBotFrom(w, null, type);
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public static CubeBot createCubeBotFrom(World w, CubeBot c, EnumCubeBotType type) throws Exception {
		Class cl = getClassFromCubeBotType(type);
		CubeBot newbot = (CubeBot) cl.getDeclaredConstructor(World.class).newInstance(w);
		newbot.entityInit();
		if (c != null) {
			newbot.tamed = c.tamed;
			newbot.tamer = c.tamer;
			newbot.setPositionAndRotation(c.posX, c.posY, c.posZ, c.rotationYaw, c.rotationPitch);
			newbot.ticksExisted = c.ticksExisted;
			newbot.setHealth(c.getHealth());
		}
		return newbot;
	}

	public static Class<? extends CubeBot> getClassFromCubeBotType(EnumCubeBotType type) {
		switch (type) {
		case NORMAL:
			return CubeBot.class;
		case COLLECTOR:
			return CubeBotCollector.class;
		case LUMBER:
			return CubeBotLumber.class;
		case FIGHTER:
			return CubeBotFighter.class;
		case BREEDER:
			return CubeBotBreeder.class;
		case FIXER:
			return CubeBotFixer.class;
		case FARMER:
			return CubeBotFarmer.class;
		case ARCHER:
			return CubeBotArcher.class;
		case MILKER:
			return CubeBotMilker.class;
		case BUTCHER:
			return CubeBotButcher.class;
		case SMITHY:
			return CubeBotSmithy.class;
		case CHICKBRING:
			return CubeBotChickBring.class;
		case OREFINDER:
			return CubeBotOreFinder.class;
		case MINER:
			return CubeBotMiner.class;
		case TELEPORTER:
			return CubeBotTeleporter.class;
		case SIREN:
			return CubeBotSiren.class;
		case TAMER:
			return CubeBotTamer.class;
		case GRASSER:
			return CubeBotGrasser.class;
			// case NETHER: return NetherBot.class;
		default:
			return CubeBot.class;
		}
	}
}
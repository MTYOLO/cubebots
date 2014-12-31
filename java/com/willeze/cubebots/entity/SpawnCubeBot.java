package com.willeze.cubebots.entity;

import java.util.Random;

import net.minecraft.world.World;

public class SpawnCubeBot {

	public static CubeBot spawnCubeBot(World w, CubeBotEnum type) {
		try {
			return doSpawn(w, type);
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public static CubeBot doSpawn(World w, CubeBotEnum type) throws Exception {
		Class cl = getClassFromCubeBotType(type);
		CubeBot newbot = (CubeBot) cl.getDeclaredConstructor(World.class).newInstance(w);
		return newbot;
	}

	public static Class<? extends CubeBot> getClassFromCubeBotType(CubeBotEnum type) {
		switch (type) {
		case NORMAL:
			return CubeBot.class;
		case COLLECT:
			return CubeBotCollect.class;
		case LUMBER:
			return CubeBotLumber.class;
		case MINER:
			return CubeBotMiner.class;
		case TELEPORT:
			return CubeBotTeleport.class;
		default:
			return CubeBot.class;
		}
	}

}

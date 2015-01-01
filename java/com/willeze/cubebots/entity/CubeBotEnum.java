package com.willeze.cubebots.entity;

public enum CubeBotEnum {

	NORMAL(0), COLLECT(1), LUMBER(2), MINER(3), TELEPORT(4);

	byte index;

	private CubeBotEnum(int i) {
		index = (byte) i;
	}

}
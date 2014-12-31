package com.willeze.cubebots.ai;

import java.util.List;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.entity.CubeBot;

import net.minecraft.entity.ai.EntityAIBase;

public class AIFindMarker extends EntityAIBase {
	public CubeBot bot;
	public List<BlockCoord> list;
	public int[] lastCheckPos = new int[3];
	public int dist;
	public int range;

	public AIFindMarker(CubeBot cb, List<BlockCoord> l, int r, int dist) {
		super();
		bot = cb;
		list = l;
		lastCheckPos = new int[] { (int) bot.posX, (int) bot.posY - dist * 2, (int) bot.posZ };
		this.range = r;
		this.dist = dist;
	}

	@Override
	public boolean shouldExecute() {
		return bot.canExecuteAI() && bot.getDistanceSq(lastCheckPos[0], lastCheckPos[1], lastCheckPos[2]) >= dist * dist;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		for (int x = (int) (bot.posX - range); x <= bot.posX + bot.width + range; x++) {
			for (int y = (int) (bot.posY - range); y <= bot.posY + bot.height + range; y++) {
				for (int z = (int) (bot.posZ - range); z <= bot.posZ + bot.width + range; z++) {
					if (bot.filterMarker(this, x, y, z)) {
						BlockCoord bc = new BlockCoord(x, y, z);
						if (!list.contains(shift(bc)))
							list.add(shift(bc));
					}
				}
			}
		}
		lastCheckPos[0] = (int) bot.posX;
		lastCheckPos[1] = (int) bot.posY;
		lastCheckPos[2] = (int) bot.posZ;
	}

	public BlockCoord shift(BlockCoord bc) {
		return bc;
	}
}

package com.willeze.cubebots.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.ai.EntityAIBase;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.block.BlockChestMarker;
import com.willeze.cubebots.entity.CubeBot;

public class AIFindChest extends EntityAIBase {
	
	CubeBot bot;
	public int[] lastCheckPos = new int[3];
	int dist;
	int range;

	public AIFindChest(CubeBot cb, int r, int dist) {
		super();
		bot = cb;
		lastCheckPos = new int[] { (int) bot.posX, (int) bot.posY, (int) bot.posZ };
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
					if (bot.worldObj.getBlock(x, y, z) == CubeBots.blockChestMarker && bot.worldObj.getBlock(x, y + 1, z) instanceof BlockChest) {
						
						if (!bot.markedChests.contains(new BlockCoord(x, y + 1, z)))
							bot.markedChests.add(new BlockCoord(x, y + 1, z));
						continue;
					}
				}
			}
		}
		lastCheckPos[0] = (int) bot.posX;
		lastCheckPos[1] = (int) bot.posY;
		lastCheckPos[2] = (int) bot.posZ;
	}
	
}

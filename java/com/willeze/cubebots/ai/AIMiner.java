package com.willeze.cubebots.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.entity.CubeBotMiner;

public class AIMiner extends EntityAIBase {
	CubeBotMiner bot;
	float moveSpeed;
	double range;
	List<BlockCoord> oreList = new ArrayList();
	List<BlockCoord> skipMarked = new ArrayList();
	BlockCoord choosen;
	int stay;

	public AIMiner(CubeBotMiner cb, float f, double r) {
		super();
		bot = cb;
		moveSpeed = f;
		range = r;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return bot.canExecuteAI() && canFindOre();
	}

	public boolean canFindOre() {
		if (bot.markedOre.isEmpty())
			return false;
		if (oreList.isEmpty()) {
			double dist = -1;
			BlockCoord bcoord = null;
			for (int a = 0; a < bot.markedOre.size(); a++) {
				BlockCoord bc = bot.markedOre.get(a);
				Block bl = bot.worldObj.getBlock(bc.x, bc.y, bc.z);
				if (!bot.filterMarker(null, bc.x, bc.y, bc.z) || skipMarked.contains(bc)) {
					bot.markedOre.remove(a);
				} else {
					double d = bot.getDistanceSq(bc.x, bc.y, bc.z);
					if (dist == -1 || dist < d) {
						dist = d;
						bcoord = bc;
					}
				}
			}
			if (bcoord == null)
				return false;

			int minx = (int) (bcoord.x - range);
			int maxx = (int) (bcoord.x + range);
			int miny = 1;
			int maxy = bcoord.y;
			int minz = (int) (bcoord.z - range);
			int maxz = (int) (bcoord.z + range);
			boolean foundAny = false;

			for (int y = maxy; y >= miny; y--) {
				for (int x = minx; x <= maxx; x++) {
					for (int z = minz; z <= maxz; z++) {
						Block var12 = bot.worldObj.getBlock(x, y, z);

						if (isBlockOk(var12)) {
							foundAny = true;
							oreList.add(new BlockCoord(x, y, z));
						}
					}
				}
				if (y == miny)
					skipMarked.add(bcoord);
				if (foundAny)
					break;
			}
		}
		return !oreList.isEmpty();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		updateTask();
	}

	public boolean isBlockOk(Block block) {
		return block instanceof BlockOre || block instanceof BlockRedstoneOre || block == Blocks.stone || block == Blocks.grass || block == Blocks.dirt || block == Blocks.cobblestone || block == Blocks.mossy_cobblestone;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		System.out.println("Stay: " + stay);
		if (choosen == null) {
			stay = 1;
			BlockCoord b = null;
			double dist = -1;
			for (int a = 0; a < oreList.size(); a++) {
				BlockCoord b1 = oreList.get(a);
				if (isBlockOk(bot.worldObj.getBlock(b1.x, b1.y, b1.z))) {
					double dsqr = bot.getDistanceSq(b1.x, b1.y, b1.z);
					if (dist == -1 || dsqr <= dist * dist) {
						b = b1;
						dist = Math.sqrt(dsqr);
					}
				} else {
					oreList.remove(b1);
				}
			}
			if (b != null) {
				choosen = b;
				stay = 0;
			}
		}
		if (choosen != null) {
			Block block = bot.worldObj.getBlock(choosen.x, choosen.y, choosen.z);
			if (block == null || !isBlockOk(block)) {
				oreList.remove(choosen);
				choosen = null;
				return;
			}
			bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
			if (bot.getDistanceSq(choosen.x, bot.posY, choosen.z) <= 2.56) {
				bot.getLookHelper().setLookPosition(choosen.x, choosen.y, choosen.z, 30, 30);
				if (stay > 0)
					stay--;
				if (stay <= 0) {
					int meta = bot.worldObj.getBlockMetadata(choosen.x, choosen.y, choosen.z);
					// block.breakBlock(bot.worldObj, choosen.x, choosen.y,
					// choosen.z, meta, 1);
					bot.worldObj.spawnEntityInWorld(new EntityItem(bot.worldObj, choosen.x, choosen.y, choosen.z, new ItemStack(block, 1, meta)));
					// block.dropBlockAsItem(bot.worldObj, choosen.x, choosen.y,
					// choosen.z, id, meta);
					bot.worldObj.setBlock(choosen.x, choosen.y, choosen.z, Blocks.air);
					oreList.remove(choosen);
					choosen = null;
					if (new Random().nextInt(3) <= 1) {
						System.out.println("THE MINER HAS TAKEN DAMAGE!");
					}
				}

			}
		}
	}
}

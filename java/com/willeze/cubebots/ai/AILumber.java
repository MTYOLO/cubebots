package com.willeze.cubebots.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.entity.CubeBotCollect;
import com.willeze.cubebots.entity.CubeBotLumber;

// bot.worldObj.getBlock(chosen.x, chosen.y, chosen.z) instanceof BlockLog

public class AILumber extends EntityAIBase {

	CubeBotLumber bot;
	float moveSpeed;
	double range;
	List<BlockCoord> woodList = new ArrayList();
	BlockCoord choosen;

	public AILumber(CubeBotLumber cb, float f, double r) {
		super();
		bot = cb;
		moveSpeed = f;
		range = r;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		System.out.println((bot.canExecuteAI() && canFindWood()));
		return bot.canExecuteAI() && canFindWood();
	}

	public boolean canFindWood() {
		if (woodList.isEmpty()) {
			AxisAlignedBB aabb = bot.boundingBox.expand(range, range, range);
			int var3 = MathHelper.floor_double(aabb.minX);
			int var4 = MathHelper.floor_double(aabb.maxX + 1.0D);
			int var5 = MathHelper.floor_double(aabb.minY);
			int var6 = MathHelper.floor_double(aabb.maxY + 1.0D);
			int var7 = MathHelper.floor_double(aabb.minZ);
			int var8 = MathHelper.floor_double(aabb.maxZ + 1.0D);

			for (int var9 = var3; var9 < var4; ++var9) {
				for (int var10 = var5; var10 < var6; ++var10) {
					for (int var11 = var7; var11 < var8; ++var11) {
//						Block var12 = Block.blocksList[bot.worldObj.getBlockId(var9, var10, var11)];
						Block var12 = bot.worldObj.getBlock(var9, var10, var11);

						if (var12 instanceof BlockLog) {
							System.out.println("NEW WOOD");
							woodList.add(new BlockCoord(var9, var10, var11));
						}
					}
				}
			}
		}
		return !woodList.isEmpty();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		updateTask();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		System.out.println("BEFORE CHOSEN");
		if (choosen == null) {
			System.out.println("CHOSEN = NULL");
			BlockCoord b = null;
			double dist = -1;
			for (int a = 0; a < woodList.size(); a++) {
				BlockCoord b1 = woodList.get(a);
				if (bot.worldObj.getBlock(b1.x, b1.y, b1.z) instanceof BlockLog) {
					System.out.println("INSTANCE WORKS - ONE");
					double dsqr = bot.getDistanceSq(b1.x, b1.y, b1.z);
					if (dist == -1 || dsqr <= dist * dist) {
						b = b1;
						dist = Math.sqrt(dsqr);
					}
				} else {
					woodList.remove(b1);
				}
			}
			if (b != null) {
				choosen = b;
			}
		}
		if (choosen != null) {
			System.out.println("CHOSEN CONTINUED");
//			int id = bot.worldObj.getBlockId(choosen.x, choosen.y, choosen.z);
			Block block = bot.worldObj.getBlock(choosen.x, choosen.y, choosen.z);
			if (block instanceof BlockLog == false) {
				woodList.remove(choosen);
				choosen = null;
				return;
			}
			bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
			bot.slightMoveWhenStill();
			if (bot.getDistanceSq(choosen.x, bot.posY, choosen.z) <= 2.5)
				bot.getNavigator().clearPathEntity();
			if (bot.getDistanceSq(choosen.x, bot.posY, choosen.z) <= 5) {
				bot.getLookHelper().setLookPosition(choosen.x, choosen.y, choosen.z, 30, 30);

//				BlockLog log = (BlockLog) block;
				int meta = bot.worldObj.getBlockMetadata(choosen.x, choosen.y, choosen.z);
//				log.breakBlock(bot.worldObj, choosen.x, choosen.y, choosen.z, meta, 1);
				System.out.println("DROP THE BASS!");
//				bot.worldObj.spawnEntityInWorld(new EntityItem(null, range, range, range, null));
				bot.worldObj.spawnEntityInWorld(new EntityItem(bot.worldObj, choosen.x, choosen.y, choosen.z, new ItemStack(block, 0, meta)));
//				log.dropBlockAsItem(bot.worldObj, choosen.x, choosen.y, choosen.z, 2, 2);
//				log.dropBlockAsItem(bot.worldObj, choosen.x, choosen.y, choosen.z, new ItemStack(Items.wooden_axe));
				bot.worldObj.setBlock(choosen.x, choosen.y, choosen.z, Blocks.air);
				System.out.println("SET IT TO AIR!");
				woodList.remove(choosen);
				choosen = null;
				if (new Random().nextInt(6) <= 3) {
					System.out.println("DAMAGE THE AXE/BOT/WHATEVER!");
				}
			}
		}
	}

}

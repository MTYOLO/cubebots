package com.willeze.cubebots.ai;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.entity.CubeBot;

public class AIStoreItems extends EntityAIBase {
	CubeBot bot;
	float speed;
	BlockCoord choosen;
	List itemsToStore;

	/**
	 * AIStoreItems is used to store items in marked chests
	 * @param bot - The CubeBot that is using this AI
	 * @param speed - The speed that the CubeBot will move to the items
	 * @param stores - //
	 */
	public AIStoreItems(CubeBot bot, float speed, Object... stores) {
		super();
		this.bot = bot;
		this.speed = speed;
		setMutexBits(3);
		itemsToStore = Arrays.asList(stores);
	}

	@Override
	public void resetTask() {
		super.resetTask();
		choosen = null;
	}

	@Override
	public boolean shouldExecute() {
		return bot.canExecuteAI() && !bot.markedChests.isEmpty() && !bot.isInventoryEmpty() && (bot.isInventoryFull() || bot.shouldStoreItems(40));
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		// continueDoing();
	}

	@Override
	public boolean continueExecuting() {
		return !bot.markedChests.isEmpty() && choosen != null && !bot.isInventoryEmpty();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		
		if (choosen == null) {
			double dist = -1;
			BlockCoord bcoord = null;
			for (int a = 0; a < bot.markedChests.size(); a++) {
				BlockCoord bc = bot.markedChests.get(a);
				TileEntity tile = bot.worldObj.getTileEntity(bc.x, bc.y, bc.z);
				if (tile == null || tile instanceof TileEntityChest == false || isChestFull((TileEntityChest) tile)) {
					bot.markedChests.remove(a);
				} else {
					double d = bot.getDistanceSq(bc.x, bc.y, bc.z);
					if (dist == -1 || dist * dist < d) {
						dist = Math.sqrt(d);
						bcoord = bc;
					}
				}
			}
			if (bcoord != null)
				choosen = bcoord;
		}
		if (choosen != null) {
			TileEntity tile = bot.worldObj.getTileEntity(choosen.x, choosen.y, choosen.z);
			if (tile == null || tile instanceof TileEntityChest == false || isChestFull((TileEntityChest) tile)) {
				choosen = null;
			} else {
				bot.setMoveTo(choosen.x, choosen.y, choosen.z, speed);
				bot.slightMoveWhenStill();
				if (bot.getDistanceSq(choosen.x, choosen.y, choosen.z) <= 4) {
					for (int a = 0; a < bot.inv.length; a++) {
						ItemStack is = bot.inv[a];
						if (is != null) {
							boolean itemCorrect = false;
							if (itemsToStore == null || itemsToStore.size() <= 0)
								itemCorrect = true;
							else
								for (int b = 0; b < itemsToStore.size(); b++) {
									if (itemsToStore.get(b) instanceof Class && ((Class) itemsToStore.get(b)).isInstance(is.getItem()) && bot.filterItemToStore(is)) {
										itemCorrect = true;
										break;
									}
									if (itemsToStore.get(b) instanceof ItemStack && bot.filterItemToStore(is) && ((ItemStack) itemsToStore.get(b)).getItem() == is.getItem() && (!is.getHasSubtypes() || is.getItemDamage() == ((ItemStack) itemsToStore.get(b)).getItemDamage())) {
										itemCorrect = true;
										break;
									}
								}
							if (itemCorrect)
								bot.addItemToChest(bot.inv[a], (TileEntityChest) tile);
						}
					}
					tile.getWorldObj().markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
					resetTask();
				}
			}
		}
	}

	public boolean isChestFull(TileEntityChest c) {
		for (int a = 0; a < c.getSizeInventory(); a++) {
			if (c.getStackInSlot(a) == null || c.getStackInSlot(a).stackSize > 0)
				return false;
		}
		return true;
	}
}

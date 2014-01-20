package T145.cubebots.entity.ai;

import java.util.ArrayList;
import java.util.List;

import T145.cubebots.entity.CubeBot;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class AICollect extends EntityAIBase {
	CubeBot bot;
	double range;
	float moveSpeed;

	List<EntityItem> nearbyItems = new ArrayList();
	Object[] choice;
	EntityItem currentItem;

	public AICollect(CubeBot cb, double r, float ms) {
		super();
		bot = cb;
		this.range = r;
		this.moveSpeed = ms;
		this.choice = null;
		setMutexBits(5);
	}

	public AICollect(CubeBot cb, double r, float ms, Object[] c) {
		this(cb, r, ms);
		choice = c;
	}

	@Override
	public boolean shouldExecute() {
		return bot.canExecuteAI() && !bot.isInventoryFull() && hasItemsNearby();
	}

	@Override
	public boolean continueExecuting() {
		bot.resetIdle();
		return nearbyItems != null && !nearbyItems.isEmpty() && !bot.isInventoryFull();
	}

	public boolean hasItemsNearby() {
		List<EntityItem> list = sortItems(bot.worldObj.getEntitiesWithinAABB(EntityItem.class, bot.boundingBox.expand(range, range, range)));
		if (!list.isEmpty()) {
			nearbyItems = list;
			return true;
		}
		return false;
	}

	public List<EntityItem> sortItems(List<EntityItem> list) {
		if (choice == null || choice.length <= 0) return list;

		for (int x = 0; x < list.size(); x++) {
			EntityItem e = list.get(x);
			ItemStack is = e.getEntityItem();
			if (is == null) {
				list.remove(e);
				continue;
			}
			boolean isItemOk = false;
			for (int a = 0; a < choice.length; a++) {
				ItemStack toPick = null;
				Class toPickClass = null;
				if (choice[a] instanceof ItemStack) toPick = (ItemStack) choice[a];
				if (choice[a] instanceof Class) toPickClass = (Class) choice[a];
				if (toPick != null && toPick.itemID == is.itemID && (!toPick.getItem().getHasSubtypes() || toPick.getItemDamage() == is.getItemDamage())) isItemOk = true;
				if (toPickClass != null && toPickClass.isInstance(is.getItem())) isItemOk = true;
				if (isItemOk) break;
			}
			if (!isItemOk) list.remove(e);
		}
		return list;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		continueDoing();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		continueDoing();
	}

	public void continueDoing() {
		super.updateTask();
		for (int a = 0; a < nearbyItems.size(); a++) {
			EntityItem item = nearbyItems.get(a);
			if (item == null || item.isDead) nearbyItems.remove(a);
			double dist = bot.getDistanceToEntity(item);
			if (dist > range * 4) nearbyItems.remove(a);
		}
		if (currentItem != null) {
			if (currentItem.isDead) currentItem = null; else {
				bot.setMoveTo(currentItem.posX, currentItem.posY, currentItem.posZ, moveSpeed);
				bot.slightMoveWhenStill();
				if (bot.getDistanceSqToEntity(currentItem) <= 1.6) {
					ItemStack left = bot.addItemStack(currentItem.getDataWatcher().getWatchableObjectItemStack(10));
					if (left != null && left.stackSize > 0) currentItem.setEntityItemStack(left); else {
						nearbyItems.remove(currentItem);
						currentItem.setDead();
						currentItem = null;
					}
				}
			}
		}
		if (currentItem == null) {
			double currentD = range * 3;
			EntityItem which = null;
			for (int a = 0; a < nearbyItems.size(); a++) {
				EntityItem item = nearbyItems.get(a);
				double dist = bot.getDistanceToEntity(item);
				if (dist < currentD) {
					which = item;
					currentD = dist;
				}
			}
			if (which != null) currentItem = which;
		}
	}
}
package T145.cubebots.entity.ai;

import java.util.Random;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.CubeBotButcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AIButcher extends AIInteractEntity {
	double pigRangeToMarker;
	int delay;

	public AIButcher(CubeBot cb, double r, float ms, double pigR) {
		super(cb, r, ms, EntityPig.class, true);
		pigRangeToMarker = pigR;
		delay = 80;
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && !bot.isInventoryFull();
	}

	@Override
	public boolean filter(Entity e) {
		if (e instanceof EntityPig && ((EntityPig) e).getGrowingAge() == 0 && bot instanceof CubeBotButcher) for (int a = 0; a < ((CubeBotButcher) bot).markedPig.size(); a++) if (((CubeBotButcher) bot).markedPig.get(a).getDistSqrTo(e) <= pigRangeToMarker * pigRangeToMarker) return true;
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (chosen == null || chosen.isDead) {
			if (chosen != null) nearbys.remove(chosen);
			pickNearest();
		}
		if (chosen != null) {
			bot.setMoveTo(chosen, moveSpeed);
			if (bot.getDistanceSqToEntity(chosen) <= 2.5) {
				delay--;
				if (delay <= 0) {
					delay = 80;
					int times = 1 + new Random().nextInt(2);

					for (int a = 0; a < times; a++) if (chosen.isBurning() || new Random().nextInt(8) == 0) bot.addItemStack(new ItemStack(Item.porkCooked)); else bot.addItemStack(new ItemStack(Item.porkRaw));
					EntityPig pig = new EntityPig(bot.worldObj);
					pig.setLocationAndAngles(chosen.posX, chosen.posY, chosen.posZ, 0.0F, 0.0F);
					bot.worldObj.spawnEntityInWorld(pig);
					pig.setGrowingAge(-3200);
					if (chosen.isBurning()) pig.setFire(200);
					chosen.setDead();
					nearbys.remove(chosen);
					nearbys.remove(pig);
					chosen = null;
				}
			}
		}
	}
}
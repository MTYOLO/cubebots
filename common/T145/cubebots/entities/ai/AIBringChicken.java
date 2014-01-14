package T145.cubebots.entities.ai;

import T145.cubebots.entities.CubeBot;
import T145.cubebots.entities.CubeBotChickBring;
import T145.cubebots.world.BlockCoord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;

public class AIBringChicken extends AIInteractEntity {
	BlockCoord nearestMark;
	double chickRange;

	public AIBringChicken(CubeBot cb, double r, float ms, double chickR) {
		super(cb, r, ms, EntityChicken.class, false);
		chickRange = chickR;
	}

	@Override
	public boolean shouldExecute() {
		CubeBotChickBring cb = (CubeBotChickBring) bot;
		if (nearestMark == null) {
			double dist = -1;
			for (int a = 0; a < cb.markedChick.size(); a++) if (dist == -1 || cb.markedChick.get(a).getDistSqrTo(cb) < dist) nearestMark = cb.markedChick.get(a);
		}
		return super.shouldExecute() && nearestMark != null;
	}

	@Override
	public boolean filter(Entity e) {
		if (e instanceof EntityChicken && bot instanceof CubeBotChickBring) {
			if (((CubeBotChickBring) bot).markedChick.isEmpty()) return false;

			for (int a = 0; a < ((CubeBotChickBring) bot).markedChick.size(); a++) if (((CubeBotChickBring) bot).markedChick.get(a).getDistSqrTo(e) <= chickRange * chickRange) return false;
			return true;
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (bot.riddenByEntity == null) goPickChick(); else goPlaceChick();
	}

	public void goPickChick() {
		if (chosen == null || chosen.isDead || chosen.ridingEntity != null) {
			if (chosen != null) {
				nearbys.remove(chosen);
				chosen = null;
			}
			pickNearest();
		}
		if (chosen != null) {
			for (int a = 0; a < ((CubeBotChickBring) bot).markedChick.size(); a++) {
				if (((CubeBotChickBring) bot).markedChick.get(a).getDistSqrTo(chosen) <= chickRange * chickRange) {
					nearbys.remove(chosen);
					chosen = null;
					break;
				}
			}
		}

		if (chosen != null) {
			bot.setMoveTo(chosen, moveSpeed);
			if (bot.getDistanceSqToEntity(chosen) <= 2) {
				chosen.mountEntity(bot);
				nearbys.remove(chosen);
				chosen = null;
			}
		}
	}

	public void goPlaceChick() {
		bot.setMoveTo(nearestMark.x, nearestMark.y, nearestMark.z, moveSpeed);
		bot.slightMoveWhenStill();
		if (nearestMark.getDistSqrTo(bot) <= 4) {
			bot.riddenByEntity.mountEntity(bot);
			nearestMark = null;
		}
	}
}
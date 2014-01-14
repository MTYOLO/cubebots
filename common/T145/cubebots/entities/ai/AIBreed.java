package T145.cubebots.entities.ai;

import java.util.ArrayList;
import java.util.List;

import T145.cubebots.entities.CreateCubeBot;
import T145.cubebots.entities.CubeBot;
import T145.cubebots.entities.CubeBotBreeder;
import T145.cubebots.entities.EnumCubeBotType;
import net.minecraft.entity.ai.EntityAIBase;

public class AIBreed extends EntityAIBase {
	CubeBotBreeder bot;
	double range;
	int maxBreedTime;
	int breedTime;
	int lifeCost;
	CubeBot breedingTarget;
	List<CubeBot> botsAround = new ArrayList();
	List<CubeBot> skips = new ArrayList();

	public AIBreed(CubeBotBreeder b, double r, int maxTime, int older) {
		super();
		bot = b;
		range = r;
		maxBreedTime = maxTime;
		breedTime = maxTime;
		lifeCost = older;
	}

	@Override
	public boolean shouldExecute() {
		return bot.canExecuteAI() && bot.tamed && bot.getTamer() != null && checkForNearbyBreeds();
	}

	public boolean checkForNearbyBreeds() {
		if (!botsAround.isEmpty())
			return true;
		else {
			List<CubeBot> list = bot.worldObj.getEntitiesWithinAABB(
					CubeBot.class, bot.boundingBox.expand(range, range, range));
			if (!list.isEmpty()) for (CubeBot cb : list) if (cb != bot && !cb.isDead && cb.canBeBreeded() && skips.contains(cb)) if (bot.tamed && cb.tamed && bot.getTamer() == cb.getTamer()) botsAround.add(cb);
		}
		return !botsAround.isEmpty();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		updateTask();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (breedingTarget != null && breedingTarget.isDead) resetBreed();

		if (breedingTarget == null) {
			CubeBot cb = null;
			double d = -1;
			for (CubeBot b : botsAround) {
				double di = bot.getDistanceSqToEntity(b);
				if (d == -1 || di < d * d) {
					d = Math.sqrt(di);
					cb = b;
				}
			}
			if (cb != null) breedingTarget = cb;
		}

		if (breedingTarget != null) {
			if (breedTime > 0) {
				bot.getNavigator().clearPathEntity();
				breedTime--;
			}
			if (breedTime == 0) {
				EnumCubeBotType type = breedingTarget.getType();
				CubeBot newBot = CreateCubeBot.createCubeBot(bot.worldObj, type);
				newBot.setPositionAndUpdate(bot.posX + bot.getLookVec().xCoord / 8D, bot.posY, bot.posZ + bot.getLookVec().zCoord / 8D);
				newBot.setTamer(bot.getTamer());
				bot.worldObj.spawnEntityInWorld(newBot);
				bot.ticksExisted += lifeCost;
				skips.add(newBot);
				resetBreed();
			}
		}
	}

	public void resetBreed() {
		if (breedingTarget != null) botsAround.remove(breedingTarget);
		breedingTarget = null;
		breedTime = maxBreedTime;
	}
}
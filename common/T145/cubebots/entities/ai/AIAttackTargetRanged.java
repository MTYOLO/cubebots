package T145.cubebots.entities.ai;

import java.util.Random;

import T145.cubebots.entities.CubeBot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class AIAttackTargetRanged extends EntityAIBase {
	CubeBot bot;
	float moveSpeed;
	double minDist;
	double maxDist;
	int maxDelay;
	int delay;

	public AIAttackTargetRanged(CubeBot c, double min, double max, float speed,
			int attackSpeed) {
		super();
		if (!(c instanceof IRangedAttackMob)) throw new IllegalArgumentException("CubeBot must implements IRangedAttackMob");
		bot = c;
		minDist = min;
		maxDist = max;
		moveSpeed = speed;
		maxDelay = attackSpeed;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLiving target = (EntityLiving) bot.getAttackTarget();
		if (target != null) {
			if (target.isDead) bot.setAttackTarget(null);
			if (target instanceof CubeBot && ((CubeBot) target).tamer == bot.tamer) bot.setAttackTarget(null);
		}
		return bot.canExecuteAI() && target != null && bot.doesInventoryHas(new ItemStack(Item.arrow)) && bot.getCurrentItemOrArmor(0) != null && bot.getCurrentItemOrArmor(0).getItem() instanceof ItemBow;
	}

	@Override
	public boolean continueExecuting() {
		if (bot.getAttackTarget() != null && bot.getAttackTarget().isDead) bot.setAttackTarget(null);
		return shouldExecute();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		delay = maxDelay;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (bot.getAttackTarget() == null) return;
		if (delay > 0) delay--;
		
		EntityLiving target = (EntityLiving) bot.getAttackTarget();

		Vec3 moveVec = Vec3.fakePool.getVecFromPool(0, 0, 0);
		double moveLength = 0;
		double ds = bot.getDistanceSqToEntity(bot.getAttackTarget());
		if (ds < minDist * minDist) {
			moveVec.xCoord = bot.posX - target.posX;
			moveVec.yCoord = bot.posY - target.posY;
			moveVec.zCoord = bot.posZ - target.posZ;
			moveLength = 16;
			if (new Random().nextInt(20) <= 14) delay++;
		}
		if (ds > maxDist * maxDist || !bot.getEntitySenses().canSee(bot.getAttackTarget())) {
			moveVec.xCoord = -bot.posX + target.posX;
			moveVec.yCoord = -bot.posY + target.posY;
			moveVec.zCoord = -bot.posZ + target.posZ;
			moveLength = Math.abs(16 - Math.sqrt(ds));
		}
		double vecLength = moveVec.lengthVector();
		if (vecLength != 0) {
			moveVec.xCoord = moveVec.xCoord / vecLength * moveLength;
			moveVec.yCoord = moveVec.yCoord / vecLength * moveLength;
			moveVec.zCoord = moveVec.zCoord / vecLength * moveLength;
			bot.setMoveTo(bot.posX + moveVec.xCoord, bot.posY + moveVec.yCoord, bot.posZ + moveVec.zCoord, moveSpeed);
		}

		if (delay <= 0) {
			bot.getLookHelper().setLookPositionWithEntity(bot.getAttackTarget(), 30, 45);
			if (ds <= maxDist * maxDist && bot.getEntitySenses().canSee(bot.getAttackTarget())) {
				bot.getNavigator().clearPathEntity();
				delay = maxDelay + new Random().nextInt(18) - 9;
				((IRangedAttackMob) bot).attackEntityWithRangedAttack(bot.getAttackTarget(), 0);
			}
		}
	}
}
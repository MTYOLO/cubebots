package T145.cubebots.entity.ai;

import java.util.Random;

import T145.cubebots.entity.CubeBot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class AIAttackTargetMelee extends EntityAIBase {
	CubeBot bot;
	float moveSpeed;
	double atkRange;

	public AIAttackTargetMelee(CubeBot cb, float f, double d) {
		super();
		bot = cb;
		moveSpeed = f;
		atkRange = d;
	}

	@Override
	public boolean shouldExecute() {
		EntityLiving target = (EntityLiving) bot.getAttackTarget();
		if (target != null) {
			if (target.isDead) bot.setAttackTarget(null);
			if (bot.getIsTamed() && target instanceof CubeBot && ((CubeBot) target).tamer == bot.tamer) bot.setAttackTarget(null);
			if (bot.getIsTamed()) bot.setAttackTarget(null);
		}
		return bot.canExecuteAI() && bot.canAttack() && bot.getAttackTarget() != null;
	}

	@Override
	public boolean continueExecuting() {
		if (bot.getAttackTarget() != null && bot.getAttackTarget().isDead) bot.setAttackTarget(null);
		return shouldExecute();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		updateTask();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		bot.getLookHelper().setLookPositionWithEntity(bot.getAttackTarget(), 30.0F, 30.0F);

		if (bot.getEntitySenses().canSee(bot.getAttackTarget())) bot.setMoveTo(bot.getAttackTarget(), moveSpeed);
		bot.slightMoveWhenStill();

		if (bot.getDistanceSqToEntity(bot.getAttackTarget()) <= atkRange * atkRange) {
			bot.attackEntityAsMob(bot.getAttackTarget());
			bot.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(bot), bot.getAttackStrength(bot.getAttackTarget()));
			bot.applyEntityCollision(bot.getAttackTarget());
			if (new Random().nextInt(5) <= 2) {
				ItemStack is = bot.getCurrentItemOrArmor(0);
				if (is != null && is.getItem().isDamageable()) {
					is.damageItem(1, bot);
					if (is.getItemDamage() <= 0) bot.setCurrentItemOrArmor(0, null); else bot.setCurrentItemOrArmor(0, is);
				}
			}
		}
	}
}
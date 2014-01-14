package T145.cubebots.entities.ai;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;

public class AIShootFireballAtTarget extends EntityAIBase 
{
	EntityLiving shooter;
	double minRange;
	double maxRange;
	int maxDelay;
	int delay;
	double shootSpeed;
	
	public AIShootFireballAtTarget(EntityLiving e, double min, double max, int delaying, double speed) 
	{
		shooter = e;
		minRange = min;
		maxRange = max;
		maxDelay = delaying;
		shootSpeed = speed;
	}

	@Override
	public boolean shouldExecute() 
	{
		if(delay>0) delay--;
		if(delay<=0 && shooter.getAttackTarget()!=null && !shooter.getAttackTarget().isDead && shooter.canEntityBeSeen(shooter.getAttackTarget()))
		{
			double dist = shooter.getDistanceSqToEntity(shooter.getAttackTarget());
			return dist>=minRange*minRange && dist<=maxRange*maxRange;
		}
		return false;
	}
	
	@Override
	public void updateTask() 
	{
		super.updateTask();
		delay = (int)((double)maxDelay*(new Random().nextDouble()*0.1+0.95));
		if(shooter.getAttackTarget()==null || shooter.getAttackTarget().isDead) return;
		
		double diffX = shooter.getAttackTarget().posX - shooter.posX;
		double diffY = shooter.getAttackTarget().posY - shooter.posY;
		double diffZ = shooter.getAttackTarget().posZ - shooter.posZ;
		EntityFireball fireball = new EntitySmallFireball(shooter.worldObj, shooter, diffX*shootSpeed, diffY*shootSpeed, diffZ*shootSpeed);
		shooter.worldObj.spawnEntityInWorld(fireball);
		
	}
}

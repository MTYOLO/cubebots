package T145.cubebots.entity.ai;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.CubeBotSiren;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;

public class AISiren extends AIInteractEntity
{

	public AISiren(CubeBot cb, double r, float ms, Class<? extends Entity> c,
			boolean exact)
	{
		super(cb, r, ms, c, exact);
		if(cb instanceof CubeBotSiren == false) throw new IllegalArgumentException("Must be a siren bot.");
	}
	
	@Override
	public boolean shouldExecute()
	{
		if(bot.getDataWatcher().getWatchableObjectInt(25)==1 && nearbys.isEmpty()) bot.getDataWatcher().updateObject(25, new Integer(0));
		if(bot.getDataWatcher().getWatchableObjectInt(25)==0 && !nearbys.isEmpty()) bot.getDataWatcher().updateObject(25, new Integer(1));
		return super.shouldExecute();
	}

	@Override
	public boolean filter(Entity e)
	{
		if(e instanceof EntityMob == false && e instanceof IMob == false) return false;
		if(!bot.getIsTamed() || bot.getTamer()==null) return false;
		
		EntityLiving mob = (EntityLiving) e;
		if(mob.canEntityBeSeen(bot.getTamer()) || mob.getAttackTarget() == bot.getTamer())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		for(int a=0 ; a<nearbys.size() ; a++)
		{
			EntityLiving e = (EntityLiving) nearbys.get(a);
			if(!e.canEntityBeSeen(bot.getTamer()) && e.getAttackTarget() != bot.getTamer())
			{
				nearbys.remove(a);
			}
			if(bot.getTamer().getDistanceSqToEntity(e)>range*range)
			{
				nearbys.remove(a);
			}
		}
		bot.getNavigator().tryMoveToEntityLiving(bot.getTamer(), moveSpeed);
	}
}

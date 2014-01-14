package T145.cubebots.entities.ai;

import java.util.List;

import T145.cubebots.entities.CubeBadZombie;
import T145.cubebots.entities.CubeBot;
import T145.cubebots.entities.CubeBotFighter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class AIFindTarget extends EntityAIBase
{
	CubeBot bot;
	Class theClass;
	Class theExcludeClass;
	double range;
	boolean oneLevelInstance;
	
	public AIFindTarget(CubeBot cb, Class<? extends EntityLiving> c, double r)
	{
		this(cb, c, r, false);
	}

	public AIFindTarget(CubeBot cb, Class<? extends EntityLiving> c, Class<? extends EntityLiving> cExclude, double r)
	{
		this(cb, c, cExclude, r, false);
	}

	public AIFindTarget(CubeBot cb, Class<? extends EntityLiving> c, Class<? extends EntityLiving> cExclude, double r, boolean ols)
	{
		this(cb, c, r, ols);
		theExcludeClass = cExclude;
	}
	
	public AIFindTarget(CubeBot cb, Class<? extends EntityLiving> c, double r, boolean oneLevelSub)
	{
		super();
		bot = cb;
		theClass = c;
		range = r;
		oneLevelInstance = oneLevelSub;
	}

	public AIFindTarget(CubeBadZombie cb, Class<EntityPlayer> class1, float r) {
		super();
		bot = cb;
		theClass = class1;
		range = r;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving target = (EntityLiving) bot.getAttackTarget();
		if(target!=null)
		{
			if(target.isDead) bot.setAttackTarget(null);
			if(bot.getIsTamed() && target instanceof CubeBot && ((CubeBot)target).getTamer() == bot.getTamer()) bot.setAttackTarget(null);
			if(bot.getIsTamed()) bot.setAttackTarget(null);
		}
		return bot.canExecuteAI() && (bot.getAttackTarget()==null || bot.getAttackTarget().isDead);
	}
	
	@Override
	public void startExecuting()
	{
		super.startExecuting();
		continueDoing();
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		continueDoing();
	}
	
	public void continueDoing()
	{
		if(bot.getAttackTarget()==null || bot.getAttackTarget().isDead)
		{
			if(bot instanceof CubeBotFighter && bot.tamed && bot.getTamer()!=null && bot.getTamer().getLastAttacker()!=null && !bot.getTamer().getLastAttacker().isDead) 
			{
				bot.setAttackTarget(bot.getTamer().getLastAttacker());
				return;
			}
			List l = bot.worldObj.getEntitiesWithinAABB(theClass, bot.boundingBox.expand(range, range, range));
			if(bot.getIsTamed() && bot.getTamer()!=null)
			{
				List<EntityLiving> l2 = bot.worldObj.getEntitiesWithinAABB(EntityLiving.class, bot.getTamer().boundingBox.expand(range, range, range));
				if(!l2.isEmpty())
				{
					for(EntityLiving e : l2)
					{
						if((e.getAttackTarget() == bot.getTamer() || e.getLastAttacker() == bot.getTamer()))
						{
							if(e instanceof CubeBot && ((CubeBot)e).getTamer() == bot.getTamer()) continue;
							else l.add(e);
						}
					}
				}
			}
			
			if(!l.isEmpty())
			{
				EntityLiving e = null;
				double d = (range+bot.width+bot.height)*(range+bot.width+bot.height);
				for(int a=0 ; a<l.size() ; a++)
				{
					EntityLiving e1 = (EntityLiving) l.get(a);
					if(theExcludeClass==null)
					{
						if(oneLevelInstance && e1.getClass()!=theClass) continue;
					}
					else
					{
						if(oneLevelInstance?e1.getClass()==theExcludeClass : theExcludeClass.isInstance(e1)) continue;
					}
					if(e1.isDead) continue;
					if(bot.getIsTamed()) continue;
					double d2 = bot.getDistanceSqToEntity(e1);
					if(d2<d)
					{
						d = d2;
						e = e1;
					}
				}
				if(e!=null)
				{
					bot.setAttackTarget(e);
					return;
				}
			}
			
			if(bot.getLastAttacker()!=null) 
			{
				bot.setAttackTarget(bot.getLastAttacker());
				return;
			}
		}
	}
}
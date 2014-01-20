package T145.cubebots.entity.ai;

import java.util.ArrayList;
import java.util.List;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.CubeBotChickBring;
import T145.cubebots.entity.CubeBotTamer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class AIInteractEntity extends EntityAIBase
{
	CubeBot bot;
	double range;
	float moveSpeed;
	Class<? extends Entity> classToSearch;
	boolean exactClass;
	List<Entity> nearbys = new ArrayList();
	Entity chosen;
	
	public AIInteractEntity(CubeBot cb, double r, float ms, Class<? extends Entity> c, boolean exact)
	{
		super();
		bot = cb;
		range = r;
		moveSpeed = ms;
		classToSearch = c;
		exactClass = exact;
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return bot.canExecuteAI() && searchForEntities();
	}

	public boolean searchForEntities()
	{
		if(nearbys.isEmpty())
		{
			List<Entity> list = bot.worldObj.getEntitiesWithinAABB(classToSearch, bot.boundingBox.expand(range, range, range));
			list.remove(bot);
			for(int a=0 ; a<list.size() ; a++)
			{
				if(exactClass && list.get(a).getClass() != classToSearch)
				{
					list.remove(a);
					continue;
				}
				if(list.get(a)==null || list.get(a).isDead) 
				{
					list.remove(a);
					continue;
				}
				if(!filter(list.get(a))) 
				{
					list.remove(a);
					continue;
				}
				nearbys.add(list.get(a));
			}
		}
		return !nearbys.isEmpty();
	}
	
	public void pickNearest()
	{
		double dist = -1;
		Entity e = null;
		for(int a=0 ; a<nearbys.size() ; a++)
		{
			double ds = bot.getDistanceSqToEntity(nearbys.get(a));
			if(nearbys.get(a)==null || nearbys.get(a).isDead || ds>range*range*4) 
			{
				nearbys.remove(a);
				continue;
			}
			if(dist==-1 || ds<dist)
			{
				dist = ds;
				e = nearbys.get(a);
			}
		}
		chosen = e;
	}
	
	public boolean filter(Entity e)
	{
		return true;
	}
	
	@Override
	public void startExecuting()
	{
		super.startExecuting();
		updateTask();
	}
}

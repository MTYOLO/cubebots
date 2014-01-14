package T145.cubebots.entities.ai;

import java.util.ArrayList;
import java.util.List;

import T145.cubebots.CubeBots;
import T145.cubebots.entities.CubeBot;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class AIYoungerizeCubeBot extends EntityAIBase
{
	CubeBot bot;
	List<CubeBot> botsList = new ArrayList();
	CubeBot target;
	float moveSpeed;
	double minDist;
	double maxDist;
	
	public AIYoungerizeCubeBot(CubeBot cb, float f, double min, double max)
	{
		super();
		bot = cb;
		moveSpeed = f;
		minDist = min;
		maxDist = max;
        setMutexBits(4);
	}
	
	@Override
	public boolean shouldExecute()
	{
		if(bot.canExecuteAI() && !bot.isInventoryEmpty() && bot.doesInventoryHas(new ItemStack(CubeBots.lifeCore)))
		{
			if(target!=null && target.isDead) target=null;
			if(target!=null) return true;
			
			for(CubeBot cb : botsList)
			{
				if(cb==null || cb.isDead || bot.getDistanceSqToEntity(cb)>maxDist*maxDist) botsList.remove(cb);
			}
			if(!botsList.isEmpty()) return true;
			
			List l = bot.worldObj.getEntitiesWithinAABB(CubeBot.class, bot.boundingBox.expand(maxDist, maxDist, maxDist));
			l.remove(bot);
			if(!l.isEmpty())
			{
				List l2 = new ArrayList();
				for(int a=0 ; a<l.size() ; a++)
				{
					CubeBot cbot = (CubeBot) l.get(a);
					if(cbot!=bot && cbot.lifeSpan>=0 && cbot.lifeSpan-cbot.ticksExisted<=1200)
					{
						l2.add(cbot);
					}
				}
				botsList.addAll(l2);
				return !botsList.isEmpty();
			}
		}
		return false;
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(target!=null && target.isDead) target=null;
		CubeBot theBot = null;
		if(target==null)
		{
			double d = maxDist+bot.width;
			for(CubeBot cb : botsList)
			{
				double di = bot.getDistanceSqToEntity(cb);
				if(di<d*d)
				{
					d = Math.sqrt(di);
					theBot = cb;
				}
			}
			target = theBot;
		}
		else theBot = target;
		
		if(theBot!=null)
		{
			bot.setMoveTo(theBot, moveSpeed);
			if(bot.getDistanceSqToEntity(theBot)<=minDist*minDist)
			{
				for(int a=0 ; a<bot.inv.length ; a++)
				{
					if(theBot.lifeSpan<0 || theBot.lifeSpan-theBot.ticksExisted>1200) break;
					
					ItemStack is = bot.inv[a];
					if(is!=null && is.getItem() == CubeBots.lifeCore)
					{
						for(int b=0 ; b<is.stackSize ; b++)
						{
							if(theBot.lifeSpan<0 || theBot.lifeSpan-theBot.ticksExisted>1200) break;
							
							theBot.lifeSpan+=22000;
							bot.inv[a].stackSize--;
							if(bot.inv[a].stackSize<=0) bot.inv[a] = null;
						}
					}
				}
				botsList.remove(target);
				theBot = target = null;
			}
		}
	}
}

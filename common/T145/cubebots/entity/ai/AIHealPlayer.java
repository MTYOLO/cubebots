package T145.cubebots.entity.ai;

import T145.cubebots.entity.CubeBot;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class AIHealPlayer extends EntityAIBase
{
	CubeBot bot;
	float moveSpeed;
	double minDist;
	double maxDist;
	int healthThreshold;
	
	public AIHealPlayer(CubeBot cb, float f, double min, double max, int threshold)
	{
		super();
		bot = cb;
		moveSpeed = f;
		minDist = min;
		maxDist = max;
		healthThreshold = threshold;
        setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute()
	{
		if(bot.getIsTamed() && bot.getTamer()!=null && bot.canExecuteAI() && bot.canFollowTamer() && bot.doesInventoryHas(ItemFood.class)) 
		{
			double distSqr = bot.getDistanceSqToEntity(bot.getTamer());
			if(distSqr<=maxDist*maxDist) 
			{
				boolean shouldHeal = false;
				if(healthThreshold==-1) shouldHeal = bot.getTamer().getHealth() < bot.getTamer().getMaxHealth();
				else shouldHeal = bot.getTamer().getHealth()<=healthThreshold;
				return shouldHeal;
			}
		}
		return false;
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		bot.setMoveTo(bot.getTamer(), moveSpeed);
		
		if(bot.getDistanceSqToEntity(bot.getTamer())<=minDist*minDist)
		{
			for(int a=0 ; a<bot.inv.length ; a++)
			{
				if(bot.getTamer().getHealth()>=bot.getTamer().getMaxHealth()-3) break;
				
				ItemStack is = bot.inv[a];
				if(is!=null && is.getItem() instanceof ItemFood)
				{
					for(int b=0 ; b<is.stackSize ; b++)
					{
						if(bot.getTamer().getHealth()>=bot.getTamer().getMaxHealth()-3) break;
						
						bot.getTamer().heal(((ItemFood)is.getItem()).getHealAmount());
						bot.inv[a].stackSize--;
						if(bot.inv[a].stackSize<=0) bot.inv[a] = null;
					}
				}
			}
		}
	}
}

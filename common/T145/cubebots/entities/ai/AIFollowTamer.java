package T145.cubebots.entities.ai;

import T145.cubebots.entities.CubeBot;
import net.minecraft.entity.ai.EntityAIBase;

public class AIFollowTamer extends EntityAIBase
{
	CubeBot bot;
	float moveSpeed;
	double minDist;
	double maxDist;
	
	public AIFollowTamer(CubeBot cb, float f, double min, double max)
	{
		super();
		bot = cb;
		moveSpeed = f;
		minDist = min;
		maxDist = max;
        this.setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute()
	{
		if(bot.tamed && bot.getTamer()!=null) 
		{
			double distSqr = bot.getDistanceSqToEntity(bot.getTamer());
			if(distSqr>=minDist*minDist && distSqr<=maxDist*maxDist) return bot.canExecuteAI() && bot.canFollowTamer();
			else return false;
		}
		else return false;
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(bot.getNavigator().noPath()) bot.setMoveTo(bot.getTamer(), moveSpeed);
	}
}

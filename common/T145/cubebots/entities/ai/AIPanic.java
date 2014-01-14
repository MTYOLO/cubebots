package T145.cubebots.entities.ai;

import T145.cubebots.entities.CubeBot;
import net.minecraft.entity.ai.EntityAIPanic;

public class AIPanic extends EntityAIPanic
{
	CubeBot b;
	
	public AIPanic(CubeBot par1EntityCreature, float par2)
	{
		super(par1EntityCreature, par2);
		b = par1EntityCreature;
	}
	
	
	@Override
	public boolean shouldExecute()
	{
		if(b.tamed && b.getLastAttacker()!=null && b.getLastAttacker()==b.getTamer()) return false;
		
		return super.shouldExecute() && b.canPanic();
	}
}

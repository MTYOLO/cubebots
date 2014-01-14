package T145.cubebots.entities;

import T145.cubebots.entities.ai.AIBreed;
import net.minecraft.world.World;

public class CubeBotBreeder extends CubeBot
{
	public CubeBotBreeder(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotBreeder.png";
		setType(EnumCubeBotType.BREEDER);
		setScale(110);
		
		tasks.addTask(3, new AIBreed(this, 8, 3000, 4800));
	}
	
	@Override
	public boolean canBeBreeded()
	{
		if(super.canBeBreeded()) return rand.nextInt(120)==0;
		return false;
	}
}

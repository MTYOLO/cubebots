package T145.cubebots.entities;

import T145.cubebots.entities.ai.AIFindOre;
import net.minecraft.world.World;

public class CubeBotOreFinder extends CubeBot
{
	public CubeBotOreFinder(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotOreFinder.png";
		setType(EnumCubeBotType.OREFINDER);

		tasks.addTask(3, new AIFindOre(this, 24, 2, 0.26F));
	}
}

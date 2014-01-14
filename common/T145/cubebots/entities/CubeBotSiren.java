package T145.cubebots.entities;

import T145.cubebots.entities.ai.AIFollowTamer;
import T145.cubebots.entities.ai.AISiren;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class CubeBotSiren extends CubeBot
{

	public CubeBotSiren(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotSiren.png";
		setType(EnumCubeBotType.SIREN);
		
		dataWatcher.addObject(25, new Integer(0));

		tasks.addTask(3, new AISiren(this, 16, 0.26F, EntityLiving.class, false));
		tasks.addTask(4, new AIFollowTamer(this, 0.23F, 4, 24));
	}
}

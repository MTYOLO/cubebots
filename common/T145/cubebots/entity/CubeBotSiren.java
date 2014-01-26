package T145.cubebots.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFollowTamer;
import T145.cubebots.entity.ai.AISiren;

public class CubeBotSiren extends CubeBot {
	public CubeBotSiren(World w) {
		super(w);
		setType(EnumCubeBotType.SIREN);

		dataWatcher.addObject(25, new Integer(0));

		tasks.addTask(3, new AISiren(this, 16, 0.26F, EntityLiving.class, false));
		tasks.addTask(4, new AIFollowTamer(this, 0.23F, 4, 24));
	}
}
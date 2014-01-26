package T145.cubebots.entity;

import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFindOre;

public class CubeBotOreFinder extends CubeBot {
	public CubeBotOreFinder(World w) {
		super(w);
		setType(EnumCubeBotType.OREFINDER);

		tasks.addTask(3, new AIFindOre(this, 24, 2, 0.26F));
	}
}
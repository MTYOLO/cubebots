package T145.cubebots.entity;

import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIBreed;

public class CubeBotBreeder extends CubeBot {
	public CubeBotBreeder(World w) {
		super(w);
		setType(EnumCubeBotType.BREEDER);
		setScale(110);

		tasks.addTask(3, new AIBreed(this, 8, 3000, 4800));
	}

	@Override
	public boolean canBeBreeded() {
		if (super.canBeBreeded())
			return rand.nextInt(120) == 0;
		return false;
	}
}
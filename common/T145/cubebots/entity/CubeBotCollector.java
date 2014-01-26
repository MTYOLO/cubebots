package T145.cubebots.entity;

import net.minecraft.world.World;
import T145.cubebots.entity.ai.AICollect;
import T145.cubebots.entity.ai.AIStoreItems;

public class CubeBotCollector extends CubeBot {
	public CubeBotCollector(World w) {
		super(w);
		setType(EnumCubeBotType.COLLECTOR);
		tasks.addTask(3, new AICollect(this, 12, 0.245F));
		tasks.addTask(3, new AIStoreItems(this, 0.25F));
	}
}
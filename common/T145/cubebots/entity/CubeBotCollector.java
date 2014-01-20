package T145.cubebots.entity;

import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AICollect;
import T145.cubebots.entity.ai.AIStoreItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CubeBotCollector extends CubeBot
{
	public CubeBotCollector(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotCollector.png";
		setType(EnumCubeBotType.COLLECTOR);
		tasks.addTask(3, new AICollect(this, 12, 0.245F));
		tasks.addTask(3, new AIStoreItems(this, 0.25F));
	}
}

package T145.cubebots.entity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIGrasser;

public class CubeBotGrasser extends CubeBot {
	public CubeBotGrasser(World w) {
		super(w);
		setType(EnumCubeBotType.GRASSER);
		setCurrentItemOrArmor(0, new ItemStack(Item.shears));

		tasks.addTask(3, new AIGrasser(this, 24, 4, 0.22F, Block.tallGrass));
	}
}
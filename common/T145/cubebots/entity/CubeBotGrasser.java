package T145.cubebots.entity;

import T145.cubebots.entity.ai.AIGrasser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CubeBotGrasser extends CubeBot
{

	public CubeBotGrasser(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotGrasser.png";
		setType(EnumCubeBotType.GRASSER);
		setCurrentItemOrArmor(0, new ItemStack(Item.shears));

		tasks.addTask(3, new AIGrasser(this, 24, 4, 0.22F, Block.tallGrass));
	}

}

package T145.cubebots.entity;

import T145.cubebots.entity.ai.AICollect;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AILumber;
import T145.cubebots.entity.ai.AIStoreItems;
import net.minecraft.block.BlockLog;
import net.minecraft.item.ItemAxe;
import net.minecraft.world.World;

public class CubeBotLumber extends CubeBot
{

	public CubeBotLumber(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotLumber.png";
		setType(EnumCubeBotType.LUMBER);

		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[]{ItemAxe.class});
		tasks.addTask(3, find);
		tasks.addTask(3, new AIGetItemFromChest(find, 0.23F, new Object[]{ItemAxe.class, new Integer[]{1}}));
		tasks.addTask(4, new AILumber(this, 0.25F, 12D));
	}

	@Override
	public boolean shouldGoFindItemInChest()
	{
		return getCurrentItemOrArmor(0) == null;
	}
}

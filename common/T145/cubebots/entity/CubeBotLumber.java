package T145.cubebots.entity;

import net.minecraft.item.ItemAxe;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AILumber;

public class CubeBotLumber extends CubeBot {
	public CubeBotLumber(World w) {
		super(w);
		setType(EnumCubeBotType.LUMBER);

		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[] { ItemAxe.class });
		tasks.addTask(3, find);
		tasks.addTask(3, new AIGetItemFromChest(find, 0.23F, new Object[] { ItemAxe.class, new Integer[] { 1 } }));
		tasks.addTask(4, new AILumber(this, 0.25F, 12D));
	}

	@Override
	public boolean shouldGoFindItemInChest() {
		return getCurrentItemOrArmor(0) == null;
	}
}
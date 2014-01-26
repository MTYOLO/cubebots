package T145.cubebots.entity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AIMilkCows;
import T145.cubebots.entity.ai.AIStoreItems;

public class CubeBotMilker extends CubeBot {
	public CubeBotMilker(World w) {
		super(w);
		setType(EnumCubeBotType.MILKER);

		tasks.addTask(3, new AIMilkCows(this, 16, 0.24F));
		tasks.addTask(4, new AIStoreItems(this, 0.24F, new ItemStack(Item.bucketMilk)));
		AIFindChestWithItem find = new AIFindChestWithItem(this, new ItemStack[] { new ItemStack(Item.bucketEmpty) });
		tasks.addTask(5, new AIGetItemFromChest(find, 0.23F, new Object[] { new ItemStack(Item.bucketEmpty), new Integer[] { 0 } }));
		tasks.addTask(5, find);
	}

	@Override
	public boolean shouldGoFindItemInChest() {
		return !doesInventoryHas(new ItemStack(Item.bucketEmpty)) && !isInventoryFull();
	}
}
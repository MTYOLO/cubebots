package T145.cubebots.entity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AIPlantAndGrow;

public class CubeBotFarmer extends CubeBot {
	public CubeBotFarmer(World w) {
		super(w);
		setType(EnumCubeBotType.FARMER);

		tasks.addTask(3, new AIPlantAndGrow(this, 16, 4, 0.22F, Block.tilledField));

		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[] { ItemSeedFood.class }, new ItemStack[] { new ItemStack(Item.dyePowder, 1, 15), new ItemStack(Item.seeds) });
		tasks.addTask(4, new AIGetItemFromChest(find, 0.22F, new Object[] { new ItemStack(Item.dyePowder, 1, 15), new Integer[] { 0 }, new ItemStack(Item.seeds), new Integer[] { 0 }, ItemSeedFood.class, new Integer[] { 0 } }));
		tasks.addTask(4, find);
	}

	@Override
	public boolean hasNeededItem() {
		return (doesInventoryHas(new ItemStack(Item.seeds)) || doesInventoryHas(ItemSeedFood.class)) && doesInventoryHas(new ItemStack(Item.dyePowder, 1, 15));
	}

	public boolean shouldGoFindItemInChest() {
		return !isInventoryFull() && !hasNeededItem();
	}
}
package T145.cubebots.entity;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AIRepairTools;
import T145.cubebots.entity.ai.AIStoreItems;

public class CubeBotSmithy extends CubeBot {
	public CubeBotSmithy(World par1World) {
		super(par1World);
		setType(EnumCubeBotType.SMITHY);
		setScale(86);

		tasks.addTask(3, new AIRepairTools(this, 12, 8, 0.23F));
		tasks.addTask(4, new AIStoreItems(this, 0.24F, ItemTool.class, ItemSword.class, ItemHoe.class));
		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[] { ItemTool.class, ItemSword.class, ItemHoe.class });
		tasks.addTask(5, new AIGetItemFromChest(find, 0.23F, new Object[] { ItemTool.class, new Integer[] { 0 }, ItemSword.class, new Integer[] { 0 }, ItemHoe.class, new Integer[] { 0 } }));
		tasks.addTask(5, find);
	}

	@Override
	public boolean shouldGoFindItemInChest() {
		return !doesInventoryHas(ItemTool.class) && !doesInventoryHas(ItemSword.class) && !doesInventoryHas(ItemHoe.class);
	}

	@Override
	public boolean hasNeededItem() {
		for (int a = 0; a < inv.length; a++)
			if (filterItemToGet(inv[a]))
				return true;
		return false;
	}

	@Override
	public boolean filterItemToGet(ItemStack is) {
		return is != null && is.getItem().isDamageable() && is.getItemDamage() > 0;
	}

	@Override
	public boolean filterItemToStore(ItemStack is) {
		return is != null && is.getItemDamage() == 0;
	}
}
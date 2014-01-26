package T145.cubebots.entity;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AIHealCubeBot;
import T145.cubebots.entity.ai.AIHealPlayer;
import T145.cubebots.entity.ai.AIYoungerizeCubeBot;

public class CubeBotFixer extends CubeBot {
	public CubeBotFixer(World w) {
		super(w);
		setType(EnumCubeBotType.FIXER);

		tasks.addTask(3, new AIHealPlayer(this, 0.28F, 3, 32, 7));
		tasks.addTask(3, new AIHealCubeBot(this, 0.25F, 3, 16));
		tasks.addTask(3, new AIYoungerizeCubeBot(this, 0.25F, 3, 16));
		tasks.addTask(4, new AIHealPlayer(this, 0.25F, 3, 16, -1));

		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[] { ItemFood.class }, new ItemStack[] { new ItemStack(CubeBots.cubePiece), new ItemStack(CubeBots.lifeCore) });
		tasks.addTask(5, new AIGetItemFromChest(find, 0.23F, new Object[] { ItemFood.class, new Integer[] { 0 }, new ItemStack(CubeBots.cubePiece), new Integer[] { 0 }, new ItemStack(CubeBots.lifeCore), new Integer[] { 0 } }));
		tasks.addTask(5, find);
	}

	public boolean shouldGoFindItemInChest() {
		return !isInventoryFull() && (!doesInventoryHas(ItemFood.class) || !doesInventoryHas(new ItemStack(CubeBots.cubePiece)) || !doesInventoryHas(new ItemStack(CubeBots.lifeCore)));
	}
}
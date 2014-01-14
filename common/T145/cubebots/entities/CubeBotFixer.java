package T145.cubebots.entities;

import T145.cubebots.CubeBots;
import T145.cubebots.entities.ai.AIFindChestWithItem;
import T145.cubebots.entities.ai.AIGetItemFromChest;
import T145.cubebots.entities.ai.AIHealCubeBot;
import T145.cubebots.entities.ai.AIHealPlayer;
import T145.cubebots.entities.ai.AIYoungerizeCubeBot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class CubeBotFixer extends CubeBot
{

	public CubeBotFixer(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotFixer.png";
		setType(EnumCubeBotType.FIXER);
		
		tasks.addTask(3, new AIHealPlayer(this, 0.28F, 3, 32, 7));
		tasks.addTask(3, new AIHealCubeBot(this, 0.25F, 3, 16));
		tasks.addTask(3, new AIYoungerizeCubeBot(this, 0.25F, 3, 16));
		tasks.addTask(4, new AIHealPlayer(this, 0.25F, 3, 16, -1));
		
		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[]{ItemFood.class}, new ItemStack[]{new ItemStack(CubeBots.cubePiece), new ItemStack(CubeBots.lifeCore)});
		tasks.addTask(5, new AIGetItemFromChest(find, 0.23F, new Object[]{
				ItemFood.class, new Integer[]{0}, new ItemStack(CubeBots.cubePiece), new Integer[]{0}, new ItemStack(CubeBots.lifeCore), new Integer[]{0}
				}));
		tasks.addTask(5, find);
	}
	
	public boolean shouldGoFindItemInChest()
	{
		return !isInventoryFull() && (!doesInventoryHas(ItemFood.class) || !doesInventoryHas(new ItemStack(CubeBots.cubePiece)) || !doesInventoryHas(new ItemStack(CubeBots.lifeCore)));
	}
}

package T145.cubebots.objects;

import T145.cubebots.CubeBots;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabCubeBot extends CreativeTabs
{

	public CreativeTabCubeBot(String label)
	{
		super(label);
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(CubeBots.cubeBotSpawn);
	}
}

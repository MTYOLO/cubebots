package T145.cubebots.entity.ai;

import T145.cubebots.entity.CubeBot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AIMilkCows extends AIInteractEntity
{
	int delay;
	
	public AIMilkCows(CubeBot cb, double r, float ms)
	{
		super(cb, r, ms, EntityCow.class, true);
	}

	@Override
	public boolean shouldExecute()
	{
		return super.shouldExecute() && bot.doesInventoryHas(new ItemStack(Item.bucketEmpty));
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(chosen==null || chosen.isDead)
		{
			if(chosen!=null) nearbys.remove(chosen);
			pickNearest();
		}
		
		if(chosen!=null)
		{
			bot.setMoveTo(chosen, moveSpeed);
			if(bot.getDistanceSqToEntity(chosen)<=2.5)
			{
				delay--;
				if(delay<=0)
				{
					delay=60;
					for(int a=0 ; a<bot.inv.length ; a++)
					{
						if(bot.inv[a]!=null && bot.inv[a].getItem() == Item.bucketEmpty)
						{
							bot.inv[a].stackSize--;
							bot.addItemStack(new ItemStack(Item.bucketMilk));
							break;
						}
					}
					nearbys.remove(chosen);
					chosen = null;
				}
			}
		}
	}

	@Override
	public boolean filter(Entity e)
	{
		return true;
	}
}

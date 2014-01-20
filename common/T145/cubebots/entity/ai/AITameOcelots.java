package T145.cubebots.entity.ai;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.CubeBotTamer;
import T145.cubebots.world.BlockCoord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AITameOcelots extends AIInteractEntity
{
	BlockCoord nearestMark;
	int delay;
	
	public AITameOcelots(CubeBot cb, double r, float ms)
	{
		super(cb, r, ms, EntityOcelot.class, true);
	}

	@Override
	public boolean shouldExecute()
	{
		CubeBotTamer cb = (CubeBotTamer) bot;
		if(nearestMark==null)
		{
			double dist = -1;
			for(int a=0 ; a<cb.markedCat.size() ; a++)
			{
				if(dist==-1 || cb.markedCat.get(a).getDistSqrTo(cb)<dist) nearestMark = cb.markedCat.get(a);
			}
		}
		return super.shouldExecute() && nearestMark!=null && bot.getIsTamed() && bot.getTamer()!=null && bot.doesInventoryHas(new ItemStack(Item.fishRaw));
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
			if(bot.getDistanceSqToEntity(chosen)<=2.2)
			{
				delay--;
				if(delay<=0)
				{
					delay=40;
					for(int a=0 ; a<bot.inv.length ; a++)
					{
						if(bot.inv[a]!=null && bot.inv[a].getItem() == Item.fishRaw)
						{
							bot.inv[a].stackSize--;
							break;
						}
					}
					((EntityOcelot)chosen).setHealth(20);
					((EntityOcelot)chosen).worldObj.setEntityState(chosen, (byte)7);
					((EntityOcelot)chosen).setTamed(true);
					((EntityOcelot)chosen).setOwner(bot.tamer);
					chosen.setPosition(nearestMark.x, nearestMark.y, nearestMark.z);
					nearbys.remove(chosen);
					chosen = null;
				}
			}
		}
	}

	@Override
	public boolean filter(Entity e)
	{
		EntityOcelot wolf = (EntityOcelot) e;
		return !wolf.isTamed();
	}
}

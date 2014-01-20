package T145.cubebots.entity.ai;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.CubeBotChickBring;
import T145.cubebots.entity.CubeBotTamer;
import T145.cubebots.world.BlockCoord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AITameWolves extends AIInteractEntity
{
	BlockCoord nearestMark;
	int delay;
	
	public AITameWolves(CubeBot cb, double r, float ms)
	{
		super(cb, r, ms, EntityWolf.class, true);
	}

	@Override
	public boolean shouldExecute()
	{
		CubeBotTamer cb = (CubeBotTamer) bot;
		if(nearestMark==null)
		{
			double dist = -1;
			for(int a=0 ; a<cb.markedWolf.size() ; a++)
			{
				if(dist==-1 || cb.markedWolf.get(a).getDistSqrTo(cb)<dist) nearestMark = cb.markedWolf.get(a);
			}
		}
		return super.shouldExecute() && nearestMark!=null && bot.getIsTamed() && bot.getTamer()!=null && bot.doesInventoryHas(new ItemStack(Item.bone));
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
						if(bot.inv[a]!=null && bot.inv[a].getItem() == Item.bone)
						{
							bot.inv[a].stackSize--;
							break;
						}
					}
					((EntityWolf)chosen).setHealth(20);
					((EntityWolf)chosen).worldObj.setEntityState(chosen, (byte)7);
					((EntityWolf)chosen).setTamed(true);
					((EntityWolf)chosen).setOwner(bot.tamer);
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
		EntityWolf wolf = (EntityWolf) e;
		return !wolf.isTamed();
	}
}

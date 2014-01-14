package T145.cubebots.entities.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import T145.cubebots.entities.CubeBot;
import T145.cubebots.entities.CubeBotSmithy;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.packet.Packet62LevelSound;

public class AIRepairTools extends AIFindBlocks
{
	float moveSpeed;
	
	public AIRepairTools(CubeBot cb, double rangeH, double rangeV, float ms)
	{
		super(cb, rangeH, rangeV, Block.anvil);
		if(!(cb instanceof CubeBotSmithy))
		{
			throw new IllegalArgumentException("Must be CubeBot Repairer");
		}
		moveSpeed = ms;
		setMutexBits(4);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return super.shouldExecute() && bot.hasNeededItem();
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(choosen==null)
		{
			pickNearest();
			foundBlocks.retainAll(Arrays.asList(choosen));
		}
		if(choosen!=null)
		{
			bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
			if(choosen.getDistSqrTo(bot)<=3.5)
			{
				repairItems();
				if(bot.hasNeededItem()) foundBlocks.remove(choosen);
			}
		}
	}
	
	public void repairItems()
	{
		if(bot.ticksExisted%4!=0) return;
		
		boolean repaired = false;
		for(int a=0 ; a<bot.inv.length ; a++)
		{
			ItemStack is = bot.inv[a];
			if(is!=null && is.getItem().isDamageable() && is.getItemDamage()>0)
			{
				is.setItemDamage(is.getItemDamage()-(new Random().nextInt(4)==0?2:1));
				repaired = true;
				if(new Random().nextInt(4)==0) continue;
				else break;
			}
		}
		if(repaired && new Random().nextInt(15)==0)
		{
			bot.worldObj.playAuxSFX(1022, choosen.x, choosen.y, choosen.z, 0);
		}
	}
}

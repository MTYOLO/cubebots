package T145.cubebots.entities.ai;

import T145.cubebots.entities.CubeBot;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class AIGetItemFromChest extends EntityAIBase
{
	AIFindChestWithItem findChestAI;
	float moveSpeed;
	Object[] pickList;
	
	public AIGetItemFromChest(AIFindChestWithItem ai, float m, Object[] obj)
	{
		findChestAI = ai;
		moveSpeed = m;
		setMutexBits(4);
		pickList = obj;
		if(obj.length%2==1) new Exception("Pick items list lack of variables.").printStackTrace();
	}
	
	@Override
	public boolean shouldExecute()
	{
		return findChestAI.bot.canExecuteAI() && findChestAI.choosen!=null;
	}
	
	@Override
	public void startExecuting()
	{
		super.startExecuting();
		updateTask();
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(findChestAI.choosen == null) return;
		
		TileEntity tile = findChestAI.bot.worldObj.getBlockTileEntity(findChestAI.choosen.x, findChestAI.choosen.y, findChestAI.choosen.z);
		if(tile instanceof TileEntityChest == false)
		{
			findChestAI.choosen = null;
			return;
		}
		else
		{
			findChestAI.bot.setMoveTo(findChestAI.choosen.x, findChestAI.choosen.y, findChestAI.choosen.z, moveSpeed);
			if(findChestAI.bot.getDistanceSq(findChestAI.choosen.x, findChestAI.choosen.y, findChestAI.choosen.z) <= 6.25)
			{
				getItemFrom(findChestAI.bot, (TileEntityChest) tile);
				findChestAI.choosen = null;
			}
		}
	}
	
	public void getItemFrom(CubeBot cb, TileEntityChest tile)
	{
		for(int a=0 ; a<tile.getSizeInventory() ; a++)
		{
			ItemStack is = tile.getStackInSlot(a);
			if(is==null || !cb.filterItemToGet(is)) continue;
			Integer[] spots = null;
			boolean isItemCorrect = false;
			for(int b=0 ; b<pickList.length/2 ; b++)
			{
				if(isItemCorrect) break;
				ItemStack toPick = null;
				Class toPickClass = null;
				if(pickList[b*2] instanceof ItemStack) toPick = (ItemStack) pickList[b*2];
				if(pickList[b*2] instanceof Class) toPickClass = (Class) pickList[b*2];
				if(toPick!=null && toPick.itemID == is.itemID && (!toPick.getItem().getHasSubtypes() || toPick.getItemDamage() == is.getItemDamage())) 
				{
					isItemCorrect = true;
					spots = (Integer[]) pickList[b*2+1];
				}
				if(toPickClass!=null && toPickClass.isInstance(is.getItem())) 
				{
					isItemCorrect = true;
					spots = (Integer[]) pickList[b*2+1];
				}
			}
			if(isItemCorrect && spots!=null)
			{
				for(Integer i : spots)
				{
					if(i>0 && findChestAI.bot.getCurrentItemOrArmor(i-1)==null)
					{
						findChestAI.bot.setCurrentItemOrArmor(i-1, is);
						tile.setInventorySlotContents(a, null);
						tile.worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
					}
					if(i==0)
					{
						ItemStack remaining = findChestAI.bot.addItemStack(is);
						if(remaining==null) tile.setInventorySlotContents(a, null);
						else tile.setInventorySlotContents(a, remaining.stackSize>0? remaining : null);
						tile.worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
					}
				}
			}
		}
	}
}

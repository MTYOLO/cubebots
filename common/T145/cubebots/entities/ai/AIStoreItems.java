package T145.cubebots.entities.ai;

import java.util.Arrays;
import java.util.List;

import T145.cubebots.entities.CubeBot;
import T145.cubebots.world.BlockCoord;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class AIStoreItems extends EntityAIBase
{
	CubeBot bot;
	float moveSpeed;
	BlockCoord choosen;
	List itemsToStore;
	
	public AIStoreItems(CubeBot cb, float ms, Object... stores)
	{
		super();
		bot = cb;
		this.moveSpeed = ms;
		setMutexBits(6);
		itemsToStore = Arrays.asList(stores);
	}
	
	@Override
	public void resetTask()
	{
		super.resetTask();
		choosen = null;
	}
	
	@Override
	public boolean shouldExecute()
	{
		return bot.canExecuteAI() && !bot.markedChests.isEmpty() && !bot.isInventoryEmpty() && (bot.isInventoryFull() || bot.shouldStoreItems(40));
	}
	
	@Override
	public void startExecuting()
	{
		super.startExecuting();
		continueDoing();
	}
	
	@Override
	public boolean continueExecuting()
	{
		return !bot.markedChests.isEmpty() && choosen!=null && !bot.isInventoryEmpty();
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		continueDoing();
	}
	
	public void continueDoing()
	{
		super.updateTask();
		if(choosen==null)
		{
			double dist = -1;
			BlockCoord bcoord = null;
			for(int a=0 ; a<bot.markedChests.size() ; a++)
			{
				BlockCoord bc = bot.markedChests.get(a);
				TileEntity tile = bot.worldObj.getBlockTileEntity(bc.x, bc.y, bc.z);
				if(tile==null || tile instanceof TileEntityChest==false || isChestFull((TileEntityChest)tile))
				{
					bot.markedChests.remove(a);
				}
				else
				{
					double d = bot.getDistanceSq(bc.x, bc.y, bc.z);
					if(dist==-1 || dist*dist<d) 
					{
						dist = Math.sqrt(d);
						bcoord = bc;
					}
				}
			}
			if(bcoord!=null) choosen = bcoord;
		}
		if(choosen!=null)
		{
			TileEntity tile = bot.worldObj.getBlockTileEntity(choosen.x, choosen.y, choosen.z);
			if(tile==null || tile instanceof TileEntityChest==false || isChestFull((TileEntityChest)tile))
			{
				choosen = null;
			}
			else
			{
				bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
				bot.slightMoveWhenStill();
				if(bot.getDistanceSq(choosen.x, choosen.y, choosen.z)<=4)
				{
					for(int a=0 ; a<bot.inv.length ; a++)
					{
						ItemStack is = bot.inv[a];
						if(is!=null)
						{
							boolean itemCorrect = false;
							if(itemsToStore==null || itemsToStore.size()<=0) itemCorrect = true;
							else for(int b=0 ; b<itemsToStore.size() ; b++)
							{
								if(itemsToStore.get(b) instanceof Class && ((Class)itemsToStore.get(b)).isInstance(is.getItem()) && bot.filterItemToStore(is)) 
								{
									itemCorrect = true;
									break;
								}
								if(itemsToStore.get(b) instanceof ItemStack && bot.filterItemToStore(is) && ((ItemStack)itemsToStore.get(b)).itemID == is.itemID && (!is.getHasSubtypes() || is.getItemDamage() == ((ItemStack)itemsToStore.get(b)).getItemDamage())) 
								{
									itemCorrect = true;
									break;
								}
							}
							if(itemCorrect) bot.addItemToChest(bot.inv[a], (TileEntityChest) tile);
						}
					}
					tile.worldObj.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
					resetTask();
				}
			}
		}
	}
	
	public boolean isChestFull(TileEntityChest c)
	{
		for(int a=0 ; a<c.getSizeInventory() ; a++)
		{
			if(c.getStackInSlot(a)==null || c.getStackInSlot(a).stackSize>0) return false;
		}
		return true;
	}
}

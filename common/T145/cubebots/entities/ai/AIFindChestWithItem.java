package T145.cubebots.entities.ai;

import T145.cubebots.entities.CubeBot;
import T145.cubebots.entities.CubeBotArcher;
import T145.cubebots.world.BlockCoord;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;

public class AIFindChestWithItem extends EntityAIBase
{
	public CubeBot bot;
	public BlockCoord choosen;
	/**0: use item class ; 1: use item list ; 2: both*/
	public int findType;
	public Class<? extends Item>[] itemClass;
	public ItemStack[] itemList;
	
	public AIFindChestWithItem(CubeBot cb, Class<? extends Item>[] c)
	{
		super();
		bot = cb;
		findType = 0;
		itemClass = c;
		setMutexBits(4);
	}
	
	public AIFindChestWithItem(CubeBot cb, ItemStack[] i)
	{
		super();
		bot = cb;
		findType = 1;
		itemList = i;
		setMutexBits(4);
	}

	public AIFindChestWithItem(CubeBot cb, Class<? extends Item>[] c, ItemStack[] i)
	{
		super();
		bot = cb;
		findType = 2;
		itemList = i;
		itemClass = c;
		setMutexBits(4);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return bot.canExecuteAI() && choosen==null && !bot.isInventoryFull() && !bot.markedChests.isEmpty() && bot.shouldGoFindItemInChest();
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
		BlockCoord bc = null;
		double sqrdist = -1;
		for(BlockCoord b : bot.markedChests)
		{
			if(bot.worldObj.getBlockTileEntity(b.x, b.y, b.z) instanceof TileEntityChest)
			{
				double sqrd = bot.getDistanceSq(b.x, b.y, b.z);
				if(sqrdist==-1 || sqrd<sqrdist)
				{
					TileEntityChest chest = (TileEntityChest) bot.worldObj.getBlockTileEntity(b.x, b.y, b.z);
					boolean isChestOk = false;
					for(int a=0 ; a<chest.getSizeInventory() ; a++)
					{
						if(isChestOk) break;
						ItemStack is = chest.getStackInSlot(a);
						if(is!=null)
						{
							if(findType == 0 || findType == 2)
							{
								for(int c=0 ; c<itemClass.length ; c++)
								{
									if(itemClass[c].isInstance(is.getItem()) && bot.filterItemToGet(is))
									{
										isChestOk = true;
										break;
									}
								}
							}
							if(findType == 1 || findType == 2)
							{
								for(int c=0 ; c<itemList.length ; c++)
								{
									ItemStack is2 = itemList[c];
									if(is2.itemID == is.itemID && (!is2.getItem().getHasSubtypes() || is2.getItemDamage() == is.getItemDamage()))
									{
										isChestOk = true;
										break;
									}
								}
							}
						}
					}
					if(isChestOk)
					{
						bc = b;
						sqrdist = sqrd;
					}
				}
			}
		}
		if(bc!=null) choosen = bc;
	}
}

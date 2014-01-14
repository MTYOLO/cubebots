package T145.cubebots.entities.ai;

import javax.naming.ldap.HasControls;

import T145.cubebots.entities.CubeBot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class AIPlantAndGrow extends AIFindBlocks
{
	float moveSpeed;
	
	public AIPlantAndGrow(CubeBot cb, double rangeH, double rangeV, float ms, Block... bs)
	{
		super(cb, rangeH, rangeV, bs);
		moveSpeed = ms;
		setMutexBits(4);
	}

	@Override
	public boolean shouldExecute()
	{
		return super.shouldExecute() && bot.hasNeededItem();
	}
	
	@Override
	public boolean filter(World world, int x, int y, int z)
	{
		if(Block.blocksList[bot.worldObj.getBlockId(x, y+1, z)] instanceof BlockCrops && 
			(bot.doesInventoryHas(new ItemStack(Item.dyePowder,1,15)) || bot.worldObj.getBlockMetadata(x, y+1, z)==7))
		{
			return true;
		}
		if(bot.worldObj.getBlockId(x, y+1, z)==0 && (bot.doesInventoryHas(new ItemStack(Item.seeds)) || bot.doesInventoryHas(ItemSeedFood.class)))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(choosen==null)
		{
			pickNearest();
		}
		if(choosen!=null)
		{
			int x = choosen.x;
			int y = choosen.y+1;
			int z = choosen.z;
			if(bot.worldObj.getBlockId(x, y, z)==0 && !(bot.doesInventoryHas(new ItemStack(Item.seeds)) || bot.doesInventoryHas(ItemSeedFood.class)))
			{
				foundBlocks.remove(choosen);
				choosen = null;
				return;
			}
			if(Block.blocksList[bot.worldObj.getBlockId(x, y, z)] instanceof BlockCrops && !bot.doesInventoryHas(new ItemStack(Item.dyePowder,1,15)) && bot.worldObj.getBlockMetadata(x, y+1, z)!=7)
			{
				foundBlocks.remove(choosen);
				choosen = null;
				return;
			}
			
			bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
			bot.slightMoveWhenStill();
			double ds = bot.getDistanceSq(choosen.x, choosen.y, choosen.z);
			if(ds<=3.4)
			{
				if(Block.blocksList[bot.worldObj.getBlockId(x, y, z)] instanceof BlockCrops)
				{
					if(bot.worldObj.getBlockMetadata(x, y, z)==7)
					{
						((BlockCrops)Block.blocksList[bot.worldObj.getBlockId(x, y, z)]).dropBlockAsItem(bot.worldObj, x, y, z, bot.worldObj.getBlockMetadata(x, y, z), 1);
						bot.worldObj.setBlock(x, y, z, 0);
					}
					else if(bot.doesInventoryHas(new ItemStack(Item.dyePowder,1,15)))
					{
						bot.decreaseItemStack(new ItemStack(Item.dyePowder,1,15));
						((BlockCrops)Block.blocksList[bot.worldObj.getBlockId(x, y, z)]).fertilize(bot.worldObj, x, y, z);
					}
				}
				if(bot.worldObj.getBlockId(x, y, z)==0 && (bot.doesInventoryHas(new ItemStack(Item.seeds)) || bot.doesInventoryHas(ItemSeedFood.class)))
				{
					for(int a=0 ; a<bot.inv.length ; a++)
					{
						ItemStack is = bot.inv[a];
						if(is!=null && (is.getItem() == Item.seeds || is.getItem() instanceof ItemSeedFood))
						{
							Block soil = Block.blocksList[bot.worldObj.getBlockId(x, y-1, z)];
							if (soil != null && soil.canSustainPlant(bot.worldObj, x, y-1, z, ForgeDirection.UP, (IPlantable) is.getItem()))
							{
				                bot.worldObj.setBlock(x, y, z, ((IPlantable)is.getItem()).getPlantID(bot.worldObj, x, y, z));
				                bot.inv[a].stackSize--;
				                if(bot.inv[a].stackSize<=0) bot.inv[a] = null;
				                break;
							}
						}
					}
				}
				foundBlocks.remove(choosen);
				choosen = null;
			}
		}
	}
}

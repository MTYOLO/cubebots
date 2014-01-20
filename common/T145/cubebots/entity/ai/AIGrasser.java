package T145.cubebots.entity.ai;

import javax.naming.ldap.HasControls;

import T145.cubebots.entity.CubeBot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class AIGrasser extends AIFindBlocks
{
	float moveSpeed;
	
	public AIGrasser(CubeBot cb, double rangeH, double rangeV, float ms, Block... bs)
	{
		super(cb, rangeH, rangeV, bs);
		moveSpeed = ms;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute()
	{
		return super.shouldExecute();
	}
	
	@Override
	public boolean filter(World world, int x, int y, int z)
	{
		if(Block.blocksList[bot.worldObj.getBlockId(x, y, z)] instanceof BlockTallGrass)
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
			int y = choosen.y;
			int z = choosen.z;
			if(!filter(bot.worldObj, x, y, z))
			{
				foundBlocks.remove(choosen);
				choosen = null;
				return;
			}
			
			bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
			bot.slightMoveWhenStill();
			double ds = bot.getDistanceSq(choosen.x, choosen.y, choosen.z);
			if(ds<=2.6)
			{
				Block.blocksList[bot.worldObj.getBlockId(x, y, z)].dropBlockAsItem(bot.worldObj, x, y, z, bot.worldObj.getBlockMetadata(x, y, z), 0);
				bot.worldObj.setBlock(x, y, z, 0);
				foundBlocks.remove(choosen);
				choosen = null;
			}
		}
	}
}

package T145.cubebots.entities.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import T145.cubebots.entities.CubeBotLumber;
import T145.cubebots.world.BlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class AILumber extends EntityAIBase
{
	CubeBotLumber bot;
	float moveSpeed;
	double range;
	List<BlockCoord> woodList = new ArrayList();
	BlockCoord choosen;
	int stay;
	
	public AILumber(CubeBotLumber cb, float f, double r)
	{
		super();
		bot = cb;
		moveSpeed = f;
		range = r;
		setMutexBits(6);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return bot.canExecuteAI() && bot.getCurrentItemOrArmor(0)!=null && 
				bot.getCurrentItemOrArmor(0).getItem() instanceof ItemAxe && canFindWood();
	}
	
	public boolean canFindWood()
	{
		if(woodList.isEmpty())
		{
			AxisAlignedBB aabb = bot.boundingBox.expand(range, range, range);
			int var3 = MathHelper.floor_double(aabb.minX);
	        int var4 = MathHelper.floor_double(aabb.maxX + 1.0D);
	        int var5 = MathHelper.floor_double(aabb.minY);
	        int var6 = MathHelper.floor_double(aabb.maxY + 1.0D);
	        int var7 = MathHelper.floor_double(aabb.minZ);
	        int var8 = MathHelper.floor_double(aabb.maxZ + 1.0D);
	
	        for (int var9 = var3; var9 < var4; ++var9)
	        {
	            for (int var10 = var5; var10 < var6; ++var10)
	            {
	                for (int var11 = var7; var11 < var8; ++var11)
	                {
	                    Block var12 = Block.blocksList[bot.worldObj.getBlockId(var9, var10, var11)];
	
	                    if (var12 instanceof BlockLog)
	                    {
	                        woodList.add(new BlockCoord(var9, var10, var11));
	                    }
	                }
	            }
	        }
		}
		return !woodList.isEmpty();
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
		if(choosen==null)
		{
			stay=1;
			BlockCoord b = null;
			double dist = -1;
			for(int a=0 ; a<woodList.size() ; a++)
			{
				BlockCoord b1 = woodList.get(a);
				if(Block.blocksList[bot.worldObj.getBlockId(b1.x, b1.y, b1.z)] instanceof BlockLog)
				{
					double dsqr = bot.getDistanceSq(b1.x, b1.y, b1.z);
					if(dist==-1 || dsqr<=dist*dist)
					{
						b = b1;
						dist = Math.sqrt(dsqr);
					}
				}
				else
				{
					woodList.remove(b1);
				}
			}
			if(b!=null)
			{
				choosen = b;
				ItemAxe axe = (ItemAxe) bot.getCurrentItemOrArmor(0).getItem();
				float f = Block.wood.getBlockHardness(bot.worldObj, choosen.x, choosen.y, choosen.z) / EnumToolMaterial.valueOf(axe.getToolMaterialName()).getEfficiencyOnProperMaterial();
				stay = (int)(f * 80F);
			}
		}
		if(choosen!=null)
		{
			int id = bot.worldObj.getBlockId(choosen.x, choosen.y, choosen.z);
			if(Block.blocksList[id] instanceof BlockLog == false)
			{
				woodList.remove(choosen);
				choosen = null;
				return;
			}
			bot.setMoveTo(choosen.x, choosen.y, choosen.z, moveSpeed);
			bot.slightMoveWhenStill();
			if(bot.getDistanceSq(choosen.x, bot.posY, choosen.z)<=2.5) bot.getNavigator().clearPathEntity();
			if(bot.getDistanceSq(choosen.x, bot.posY, choosen.z)<=5)
			{
				bot.getLookHelper().setLookPosition(choosen.x, choosen.y, choosen.z, 30, 30);
				if(stay>0)stay--;
				if(stay<=0)
				{
					BlockLog log = (BlockLog) Block.blocksList[id];
					int meta = bot.worldObj.getBlockMetadata(choosen.x, choosen.y, choosen.z);
					log.breakBlock(bot.worldObj, choosen.x, choosen.y, choosen.z, meta, 1);
					log.dropBlockAsItem(bot.worldObj, choosen.x, choosen.y, choosen.z, id, meta);
					bot.worldObj.setBlock(choosen.x, choosen.y, choosen.z, 0);
					woodList.remove(choosen);
					choosen = null;
					if(new Random().nextInt(6)<=3)
					{
						ItemStack is = bot.getCurrentItemOrArmor(0);
						if(is!=null && is.getItem().isDamageable())
						{
							is.damageItem(1, bot);
							if(is.getItemDamage()<=0) bot.setCurrentItemOrArmor(0, null);
							else bot.setCurrentItemOrArmor(0, is);
						}
					}
				}
			}
		}
	}
}

package T145.cubebots.entity.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.world.BlockCoord;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class AIFindBlocks extends EntityAIBase
{
	CubeBot bot;
	double rangeH;
	double rangeV;
	List<Block> listBlock = new ArrayList();
	List<BlockCoord> foundBlocks = new ArrayList();
	BlockCoord choosen;
	
	public AIFindBlocks(CubeBot cb, double rangeH, double rangeV, Block... bs)
	{
		super();
		bot = cb;
		this.rangeH = rangeH;
		this.rangeV = rangeV;
		listBlock = Arrays.asList(bs);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return bot.canExecuteAI() && hasApplicableBlocks();
	}
	
	public boolean hasApplicableBlocks()
	{
		if(foundBlocks.isEmpty())
		{
			AxisAlignedBB aabb = bot.boundingBox.expand(rangeH, rangeV, rangeH);
			int x1 = (int) aabb.minX;
			int y1 = (int) aabb.minY;
			int z1 = (int) aabb.minZ;
			int x2 = (int) aabb.maxX+1;
			int y2 = (int) aabb.maxY+1;
			int z2 = (int) aabb.maxZ+1;
			for(int x=x1 ; x<=x2 ; x++){
			for(int y=y1 ; y<=y2 ; y++){
			for(int z=z1 ; z<=z2 ; z++){
				Block b = Block.blocksList[bot.worldObj.getBlockId(x, y, z)];
				if(listBlock.contains(b) && filter(bot.worldObj, x, y, z))
				{
					foundBlocks.add(new BlockCoord(x,y,z));
				}
			}}}
		}
		
		return !foundBlocks.isEmpty();
	}
	
	public boolean filter(World world, int x, int y, int z)
	{
		return true;
	}
	
	public void pickNearest()
	{
		BlockCoord choice = null;
		double d = -1;
		for(int a=0 ; a<foundBlocks.size() ; a++)
		{
			BlockCoord bc = foundBlocks.get(a);
			Block b = Block.blocksList[bot.worldObj.getBlockId(bc.x, bc.y, bc.z)];
			if(!listBlock.contains(b)) 
			{
				foundBlocks.remove(a);
				continue;
			}
			double d2 = bot.getDistanceSq(bc.x, bot.posY, bc.z);
			if(d2>rangeH*rangeH*4 || Math.abs(bot.posY-bc.y)>=2*rangeV)
			{
				foundBlocks.remove(a);
				continue;
			}
			if(d==-1 || d2<=d)
			{
				d = bot.getDistanceSq(bc.x, bc.y, bc.z);
				choice = bc;
			}
		}
		if(choice!=null) choosen = choice;
	}
	
	@Override
	public void startExecuting()
	{
		super.startExecuting();
		updateTask();
	}
	
}

package T145.cubebots.entity.ai;

import java.util.Random;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.world.BlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.ai.EntityAIBase;

public class AIFindOre extends EntityAIBase
{
	CubeBot bot;
	BlockCoord foundAt;
	float moveSpeed;
	double range;
	int searchRange;
	int foundTick;
	boolean found;
	int tick;
	
	public AIFindOre(CubeBot b, double r, int search, float ms)
	{
		super();
		bot = b;
		range = r;
		searchRange = search;
		moveSpeed = ms;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if(foundAt==null && !found) pickRandomCoord();
		
		return bot.canExecuteAI() && bot.getIsTamed() && bot.getTamer()!=null && found;
	}
	
	@Override
	public boolean continueExecuting()
	{
		tick++;
		return super.continueExecuting();
	}
	
	public void pickRandomCoord()
	{
		int X = (int) (bot.posX + (new Random().nextDouble()-0.5)*2*range);
		int Z = (int) (bot.posZ + (new Random().nextDouble()-0.5)*2*range);
		BlockCoord bc = new BlockCoord(X, (int) bot.posY, Z);
		foundTick = 0;
		if(canFindOresAt(bc)) 
		{
			foundAt = bc;
			found = true;
		}
		else
		{
			foundAt = null;
			found = false;
		}
	}
	
	public boolean canFindOresAt(BlockCoord bc)
	{
		int X = bc.x;
		int Z = bc.z;
		for(int y=0 ; y<bot.worldObj.getActualHeight() ; y++){
		for(int x=X-searchRange ; x<=X+searchRange ; x++){
		for(int z=Z-searchRange ; z<=Z+searchRange ; z++){
			int id = bot.worldObj.getBlockId(x, y, z);
			Block block = Block.blocksList[id];
			if(block instanceof BlockOre || block instanceof BlockRedstoneOre) return true;
		}}}
		return false;
	}
	
	@Override
	public void updateTask()
	{
		super.updateTask();
		if(found) foundTick++;
		bot.setMoveTo(foundAt.x+0.5, foundAt.y, foundAt.z+0.5, moveSpeed);
		if(bot.onGround) bot.setJumping(true);
		BlockCoord foundAtXZ = new BlockCoord(foundAt.x, (int) bot.posY, foundAt.z);
		if(foundAtXZ.getDistSqrTo(bot)<=2.65) bot.setStay(true);
	}
}
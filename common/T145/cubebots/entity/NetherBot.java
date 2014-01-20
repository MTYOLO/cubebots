package T145.cubebots.entity;
/*package T145.cubebots.entity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AIAttackTargetMelee;
import T145.cubebots.entity.ai.AIShootFireballAtTarget;

public class NetherBot extends CubeBot
{

	public NetherBot(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/netherBot.png";
		setType(EnumCubeBotType.NETHER);
		setScale(100);
		maxHealth = 20;
		isImmuneToFire = true;
		setEntityHealth(getMaxHealth());

        tasks.addTask(3, new AIShootFireballAtTarget(this, 8, 24, 30, 0.35));
        tasks.addTask(3, new AIAttackTargetMelee(this, 0.24F, 1.9));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 16.0F, 0, true));
	}
	
	@Override
    public boolean isBurning()
    {
        return false;
    }
	
	@Override
	public boolean canAttackClass(Class c)
	{
		if(c == getClass()) return false;
		return true;
	}
	
	@Override
	public int getAttackStrength(Entity e)
	{
		return 2 + rand.nextInt(3)*(rand.nextInt(2)+1);
	}

	@Override
	protected void dropFewItems(boolean recentlyAttacked, int lootModify)
	{
		if(rand.nextInt(7)<=(tamed?1:5)) dropItem(new ItemStack(CubeBots.netherPiece, new Random().nextInt(2)+1));
		if(rand.nextInt(150)==22) dropItem(new ItemStack(CubeBots.cubeBotCore));
		if(rand.nextInt(10)==0) dropItem(new ItemStack(Item.goldNugget, new Random().nextInt(2)+1));
		if(rand.nextInt(170)==0) dropItem(new ItemStack(Item.swordGold, new Random().nextInt(2)+1));
	}
	
	@Override
	public boolean additionalSpawnRequirement() 
	{
		return true;
	}
}
*/
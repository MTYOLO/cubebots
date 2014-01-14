package T145.cubebots.entities.ai;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTargetSorter;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class AINearestAttackableCubeBot extends AICubeBotTarget
{
    EntityLiving targetEntity;
    Class targetClass;
    private int targetChance;
    private final IEntitySelector field_82643_g;
	private float targetDistance;


    public AINearestAttackableCubeBot(EntityLiving par1EntityLiving, Class par2Class, float par3, int par4, boolean par5)
    {
        this(par1EntityLiving, par2Class, par3, par4, par5, false);
    }

    public AINearestAttackableCubeBot(EntityLiving par1EntityLiving, Class par2Class, float par3, int par4, boolean par5, boolean par6)
    {
        this(par1EntityLiving, par2Class, par3, par4, par5, par6, (IEntitySelector)null);
    }

    public AINearestAttackableCubeBot(EntityLiving par1, Class par2, float par3, int par4, boolean par5, boolean par6, IEntitySelector par7IEntitySelector)
    {
        super(par1, par3, par5, par6);
        this.targetClass = par2;
        this.targetDistance = par3;
        this.targetChance = par4;
        this.field_82643_g = par7IEntitySelector;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
            if (this.targetClass == EntityPlayer.class)
            {
            	List l = (List) this.taskOwner.worldObj.getClosestVulnerablePlayerToEntity(this.taskOwner, (double)this.targetDistance);
                Iterator iterator = l.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    EntityLiving entityplayer = (EntityLiving)entity;

                    if (this.isSuitableTarget(entityplayer, false))
                    {
                        this.targetEntity = entityplayer;
                        return true;
                    }
                }
            }
            else
            {
                List list = this.taskOwner.worldObj.selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand((double)this.targetDistance, 4.0D, (double)this.targetDistance), this.field_82643_g);
                Iterator iterator = list.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();
                    EntityLiving entityliving = (EntityLiving)entity;

                    if (this.isSuitableTarget(entityliving, false))
                    {
                        this.targetEntity = entityliving;
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
}
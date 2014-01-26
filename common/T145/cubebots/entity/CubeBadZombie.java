package T145.cubebots.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIFindTarget;

public class CubeBadZombie extends CubeBad {
	public CubeBadZombie(World par1World) {
		super(par1World);
		burnInLight = true;
		targetTasks.addTask(2, new AIFindTarget(this, CubeBot.class, CubeBad.class, 16.0F));
		targetTasks.addTask(2, new AIFindTarget(this, EntityPlayer.class, 16.0F));
	}

	public void onKillEntity(EntityLiving e) {
		super.onKillEntity(e);
		if (!worldObj.isRemote && e instanceof CubeBot && rand.nextInt(3) == 0) {
			worldObj.removeEntity(e);
			CubeBadZombie z = new CubeBadZombie(worldObj);
			z.setPosition(e.posX, e.posY, e.posZ);
			z.setHealth(z.getMaxHealth());
			worldObj.spawnEntityInWorld(z);
		}
	}

	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		if (d.getSourceOfDamage() != null && (d.getSourceOfDamage() instanceof EntityPlayer || d.getSourceOfDamage() instanceof CubeBot)) {
			if (rand.nextInt(3) == 0) {
				CubeBot cb = CreateCubeBot.createCubeBot(worldObj, EnumCubeBotType.NORMAL);
				cb.setPosition(posX, posY, posZ);
				worldObj.spawnEntityInWorld(cb);
			}
		}
	}
}
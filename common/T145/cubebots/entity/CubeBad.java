package T145.cubebots.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import T145.cubebots.entity.ai.AIAttackTargetMelee;
import cpw.mods.fml.common.FMLCommonHandler;

public class CubeBad extends CubeBot {
	public boolean burnInLight;

	public CubeBad(World par1World) {
		super(par1World);
		tamable = false;
		setType(EnumCubeBotType.HOSTILE);
		burnInLight = false;
		setScale(90);

		tasks.addTask(3, new AIAttackTargetMelee(this, 0.23F, getScale() + 0.4D));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
	}

	@Override
	public void addDefaultTasks() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 6.0F, 0.04F));
		tasks.addTask(9, new EntityAIWander(this, 0.2F));
		tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 10.0F));
		tasks.addTask(10, new EntityAILookIdle(this));
	}

	@Override
	public void onLivingUpdate() {
		if (burnInLight && worldObj.isDaytime() && !worldObj.isRemote && !isChild()) {
			float f = getBrightness(1.0F);

			if (f > 0.5F && rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && worldObj.canBlockSeeTheSky((int) Math.floor(posX), (int) Math.floor(posY), (int) Math.floor(posZ))) {
				setFire(8);
			}
		}

		super.onLivingUpdate();
	}

	@Override
	public int getAttackStrength(Entity e) {
		return 1 + FMLCommonHandler.instance().getMinecraftServerInstance().getDifficulty();
	}

	@Override
	public boolean additionalSpawnRequirement() {
		return isValidLightLevel();
	}

	protected boolean isValidLightLevel() {
		int i = (int) Math.floor(posX);
		int j = (int) Math.floor(boundingBox.minY);
		int k = (int) Math.floor(posZ);

		if (worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > rand.nextInt(32)) {
			return false;
		} else {
			int l = worldObj.getBlockLightValue(i, j, k);

			if (worldObj.isThundering()) {
				int i1 = worldObj.skylightSubtracted;
				worldObj.skylightSubtracted = 10;
				l = worldObj.getBlockLightValue(i, j, k);
				worldObj.skylightSubtracted = i1;
			}

			return l <= rand.nextInt(8);
		}
	}
}
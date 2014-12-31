package com.willeze.cubebots.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.ai.AICollect;
import com.willeze.cubebots.ai.AIFindChest;
import com.willeze.cubebots.ai.AIFindMarker;
import com.willeze.cubebots.ai.AIPanic;

public class CubeBot extends EntityCreature {

	public static CubeBotEnum type;
	public int maxHealth, lifeSpan, idleTicks;

	public boolean tamable, tamed;
	public String tamer;
	public ItemStack[] inv = new ItemStack[36];
	public List<BlockCoord> markedChests = new ArrayList();

	public CubeBot(World par1World) {
		super(par1World);
		type = CubeBotEnum.NORMAL;
		lifeSpan = 2400;
		idleTicks = 0;

		getNavigator().setAvoidsWater(true);

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(1, new AIPanic(this, 1.15F));
		this.tasks.addTask(2, new AIFindChest(this, 8, 10));
//		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
//		this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
//		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
//		this.tasks.addTask(6, new EntityAILookIdle(this));

//		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
//		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCow.class, 0, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	public boolean canPanic() {
		return true;
	}

	public boolean filterMarker(AIFindMarker ai, int x, int y, int z) {
		return true;
	}

	public static List getEntityItemsInRadius(World world, double x, double y, double z, int radius) {
		List list = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y, z, x + radius, y + radius, z + radius));
		return list;
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1.0f);
	}

	public boolean canExecuteAI() {
		return true;
	}

	public boolean hasNeededItem() {
		return false;
	}

	@Override
	protected boolean isMovementBlocked() {
		return super.isMovementBlocked();
	}

	public void setType(CubeBotEnum t) {
		type = t;
	}

	public CubeBotEnum getType() {
		CubeBotEnum[] all = CubeBotEnum.values();
		for (int a = 0; a < all.length; a++)
			if (all[a].index == Integer.parseInt(type.toString()))
				return all[a];

		return CubeBotEnum.NORMAL;
	}

	public void spawnParticles(String s) {
		for (int var3 = 0; var3 < 7; ++var3) {
			double var4 = this.rand.nextGaussian() * 0.02D;
			double var6 = this.rand.nextGaussian() * 0.02D;
			double var8 = this.rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(s, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, var4, var6, var8);
		}
	}

	@Override
	public boolean interact(EntityPlayer p) {
		return true;
		// if (p.getHeldItem() != null && p.getHeldItem().getItem() ==
		// CubeBots.itemCubeCall && lifeSpan >= 0) {
		// lifeSpan += 20000;
		// spawnParticles("flame");
		// if (!p.capabilities.isCreativeMode) {
		// p.getHeldItem().stackSize--;
		// if (p.getHeldItem().stackSize <= 0)
		// p.inventory.setInventorySlotContents(p.inventory.currentItem,
		// (ItemStack) null);
		// }
		// return true;
		// }
		// return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (lifeSpan >= 0 && ticksExisted > lifeSpan)
			attackEntityFrom(DamageSource.generic, getHealth());
		idleTicks++;
		if ((isJumping || isAirBorne && motionY >= 0.2) && (Math.abs(motionX) + Math.abs(motionZ)) <= 0.2) {
			Vec3 vec = getLookVec();
			motionX = vec.xCoord / 4;
			motionZ = vec.zCoord / 4;
		}
	}

	public boolean isInventoryFull() {
		for (int a = 0; a < inv.length; a++) {
			if (inv[a] != null && inv[a].stackSize == 0)
				inv[a] = null;
			if (inv[a] == null || inv[a].stackSize == 0)
				return false;
		}

		return true;
	}

	public boolean isInventoryEmpty() {
		for (int a = 0; a < inv.length; a++)
			if (inv[a] != null)
				return false;

		return true;
	}

	public boolean doesInventoryHas(Class<? extends Item> c) {
		for (int a = 0; a < inv.length; a++)
			if (inv[a] != null && c.isInstance(inv[a].getItem()))
				return true;
		return false;
	}

	public boolean doesInventoryHas(ItemStack is) {
		for (int a = 0; a < inv.length; a++) {
			if (inv[a] != null) {
				ItemStack is2 = inv[a];
				if (is2.getItem() == is.getItem() && (is2.getItemDamage() == is.getItemDamage() || is.isItemStackDamageable()))
					return true;
			}
		}
		return false;
	}

	public boolean filterItemToGet(ItemStack is) {
		return true;
	}

	public boolean filterItemToStore(ItemStack is) {
		return true;
	}

	public ItemStack addItemStack(ItemStack is) {
		if (is == null || is.stackSize <= 0)
			return null;

		for (int a = 0; a < inv.length; a++) {
			if (inv[a] == null || inv[a].stackSize <= 0) {
				inv[a] = is;
				return null;
			}
			ItemStack is2 = inv[a];
			if (is2.getItem() == is.getItem() && is2.getItemDamage() == is.getItemDamage()) {
				int amount = Math.min(is.stackSize, is2.getMaxStackSize() - is2.stackSize);
				is.stackSize -= amount;
				is2.stackSize += amount;
				inv[a] = is2;
			}
			if (is.stackSize <= 0)
				return null;
		}
		return is;
	}

	public boolean decreaseItemStack(ItemStack is) {
		if (is == null || is.stackSize <= 0)
			return true;
		for (int a = 0; a < inv.length; a++) {
			ItemStack is2 = inv[a];
			if (is2 == null)
				continue;
			if (is2.getItem() == is.getItem() && (is2.getItemDamage() == is.getItemDamage() || !is.getItem().getHasSubtypes())) {
				int amount = Math.min(is.stackSize, is2.stackSize);
				is.stackSize -= amount;
				is2.stackSize -= amount;
				if (is2.stackSize <= 0)
					is2 = null;
				inv[a] = is2;
			}
			if (is.stackSize <= 0)
				return true;
		}
		return is.stackSize > 0;
	}

	public void resetIdle() {
		idleTicks = 0;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	public void setMoveTo(Entity e, float ms) {
		setMoveTo(e.posX, e.posY, e.posZ, ms);
	}

	public void setMoveToClone(double x, double y, double z, float ms) {
		System.out.println("Move To: " + x + "," + y + "," + z + " at speed: " + ms);
	}

	public void setMoveTo(double x, double y, double z, float ms) {
		// Vec3 vec = new Vec3( x - posX, y - posY, z - posZ);
		double rate = 1;

		getNavigator().tryMoveToXYZ((int) x, (int) y, (int) z, ms);
	}

	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		if (!this.worldObj.isRemote) {
			for (int a = 0; a < inv.length; a++)
				if (inv[a] != null)
					worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY + 0.3, posZ, inv[a]));
		} else
			for (int a = 0; a < rand.nextInt(10) + 10; a++)
				worldObj.spawnParticle("flame", posX + width / 2, posY + height / 2, posZ + width / 2, (rand.nextDouble() - 0.5) / 8, rand.nextDouble() * 0.2, (rand.nextDouble() - 0.5) / 8);
	}

	@Override
	protected void dropFewItems(boolean recentlyAttacked, int lootModify) {
		super.dropFewItems(recentlyAttacked, lootModify);

		if (rand.nextInt(7) >= 5)
			dropItem(new ItemStack(CubeBots.itemInstantCubeCall, new Random().nextInt(2) + 1));
		if (rand.nextInt(150) >= 22)
			dropItem(new ItemStack(CubeBots.itemCubeCall, new Random().nextInt(2) + 1));
	}

	public void dropItem(ItemStack is) {
		entityDropItem(is, 0.6F);
	}

	public void slightMoveWhenStill() {
		if (Math.abs(motionX) + Math.abs(motionZ) <= 0.4) {
			Vec3 vec = getLookVec();
			motionX = vec.xCoord / 10;
			motionZ = vec.zCoord / 10;
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && additionalSpawnRequirement();
	}

	public boolean additionalSpawnRequirement() {
		return worldObj.isAABBInMaterial(boundingBox.expand(12, 12, 12), Material.lava);
	}

	@Override
	protected void jump() {
		super.jump();
		motionY += 0.07;
	}

	public boolean shouldStoreItems(int a) {
		return idleTicks >= a;
	}

	public void addItemToChest(ItemStack is, TileEntityChest chest) {
		if (is == null || is.stackSize <= 0 || chest == null)
			return;

		for (int a = 0; a < chest.getSizeInventory(); a++) {
			ItemStack is2 = chest.getStackInSlot(a);
			if (is2 == null || is2.stackSize <= 0) {
				chest.setInventorySlotContents(a, is.copy());
				decreaseItemStack(is);
				return;
			}
			if (is2.getItem() == is.getItem() && is2.getItemDamage() == is.getItemDamage()) {
				int amount = Math.min(is.stackSize, is2.getMaxStackSize() - is2.stackSize);
				ItemStack is3 = is.copy();
				is3.stackSize = amount;
				decreaseItemStack(is3);
				is.stackSize -= amount;
				is2.stackSize += amount;
				chest.setInventorySlotContents(a, is2);
			}
			if (is.stackSize <= 0)
				return;
		}
	}

}

package T145.cubebots.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AIFindChest;
import T145.cubebots.entity.ai.AIFindMarker;
import T145.cubebots.entity.ai.AIFollowTamer;
import T145.cubebots.entity.ai.AIPanic;
import T145.cubebots.world.BlockCoord;

public class CubeBot extends EntityCreature {
	private int scale;
	private EnumCubeBotType type;
	public int maxHealth, lifeSpan, idleTicks;

	public boolean tamable, tamed;
	public String tamer;
	public ItemStack[] inv = new ItemStack[36];
	public List<BlockCoord> markedChests = new ArrayList();

	public CubeBot(World par1World) {
		super(par1World);
		type = EnumCubeBotType.NORMAL;
		dataWatcher.addObject(19, new Byte(type.index));
		maxHealth = 14;
		setHealth(getMaxHealth());
		lifeSpan = 24000;
		dataWatcher.addObject(20, new Integer(scale));
		setScale((int) 66);
		tamable = true;
		tamed = false;
		idleTicks = 0;
		dataWatcher.addObject(21, new Byte(getAttackTarget() != null ? (byte) 1 : (byte) 0));
		dataWatcher.addObject(22, new Integer(getCurrentItemOrArmor(0) == null ? -1 : getCurrentItemOrArmor(0).itemID));
		dataWatcher.addObject(23, new Byte((byte) (isInventoryEmpty() ? 1 : 0)));
		dataWatcher.addObject(24, new Byte((byte) (tamed ? 1 : 0)));

		getNavigator().setAvoidsWater(true);
		getNavigator().setEnterDoors(true);
		addDefaultTasks();
	}

	public void addDefaultTasks() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new AIPanic(this, 0.32F));
		tasks.addTask(1, new AIFindChest(this, 8, 10));
		tasks.addTask(9, new AIFollowTamer(this, 0.23F, 8, 16));
		tasks.addTask(9, new EntityAITempt(this, 0.25F, CubeBots.cubeBotCore.itemID, false));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 6.0F, 0.04F));
		tasks.addTask(9, new EntityAIWander(this, 0.2F));
		tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 10.0F));
		tasks.addTask(10, new EntityAILookIdle(this));
	}

	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(17, new Byte((byte) 0));
		dataWatcher.addObject(18, new Integer((int) this.getHealth()));
	}

	public void setType(EnumCubeBotType t) {
		type = t;
		dataWatcher.updateObject(19, Byte.valueOf(type.index));
	}

	public EnumCubeBotType getType() {
		EnumCubeBotType[] all = EnumCubeBotType.values();
		byte b = dataWatcher.getWatchableObjectByte(19);
		for (int a = 0; a < all.length; a++) if (all[a].index == b) return all[a];

		return EnumCubeBotType.NORMAL;
	}

	/** 0.0 -> 1.0 = setScale( (int)0 -> (int)100 ) */
	public void setScale(double d) {
		setScale(d * 100);
	}

	/** 0-100 */
	public void setScale(int b) {
		scale = b;
		dataWatcher.updateObject(20, Integer.valueOf(scale));
		setSize(getScale() * 0.8F, getScale() * 0.8F);
	}

	public float getScale() {
		return (float) dataWatcher.getWatchableObjectInt(20) / 100F;
	}

	public void tryTame(EntityPlayer p) {
		setTamer(p);

		String s = "heart";
		if (!tamed) s = "smoke";
		spawnParticles(s);
	}

	public void setTamer(EntityPlayer p) {
		if (p == null) {
			tamed = false;
			tamer = "";
		} else {
			tamed = true;
			tamer = p.getEntityName();
		}
		dataWatcher.updateObject(24, new Byte((byte) (tamed ? 1 : 0)));
	}

	public boolean getIsTamed() {
		return dataWatcher.getWatchableObjectByte(24) == 1;
	}

	public EntityPlayer getTamer() {
		try {
			return worldObj.getPlayerEntityByName(tamer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean canFollowTamer() {
		return getIsTamed() && tamer != null;
	}

	public boolean canPanic() {
		return true;
	}

	public boolean hasNeededItem() {
		return false;
	}

	public boolean canBeBreeded() {
		if (ticksExisted >= 1200) return true; else return rand.nextInt(1201 - ticksExisted) == 1;
	}

	public boolean getStay() {
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	public void setStay(boolean b) {
		dataWatcher.updateObject(17, new Byte((byte) (b ? 1 : 0)));
	}

	@Override
	protected boolean isMovementBlocked() {
		return super.isMovementBlocked() || getStay();
	}

	@Override
	public boolean canBePushed() {
		return super.canBePushed() && !getStay();
	}

	public boolean canAttack() {
		return true;
	}

	public int getAttackStrength(Entity e) {
		return 1;
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
		if (tamable && p.getHeldItem() != null && p.getHeldItem().getItem() == CubeBots.cubeBotCore) {
			if (!getIsTamed()) {
				tryTame(p);
				if (!p.capabilities.isCreativeMode) {
					p.getHeldItem().stackSize--;
					if (p.getHeldItem().stackSize <= 0) p.inventory.setInventorySlotContents(p.inventory.currentItem, (ItemStack) null);
				}
				return true;
			} else {
				if (dataWatcher.getWatchableObjectInt(18) < getMaxHealth()) {
					heal(7);
					spawnParticles("heart");
					if (!p.capabilities.isCreativeMode) {
						p.getHeldItem().stackSize--;
						if (p.getHeldItem().stackSize <= 0) p.inventory.setInventorySlotContents(p.inventory.currentItem, (ItemStack) null);
					}
				}
				return true;
			}
		}
		if (p.getHeldItem() != null && p.getHeldItem().getItem() == CubeBots.lifeCore && lifeSpan >= 0) {
			lifeSpan += 20000;
			spawnParticles("flame");
			if (!p.capabilities.isCreativeMode) {
				p.getHeldItem().stackSize--;
				if (p.getHeldItem().stackSize <= 0) p.inventory.setInventorySlotContents(p.inventory.currentItem, (ItemStack) null);
			}
			return true;
		}
		if (p.getHeldItem() != null && p.getHeldItem().getItem() == CubeBots.infiniteLifeCore && lifeSpan >= 0) {
			lifeSpan = -1;
			spawnParticles("flame");
			if (!p.capabilities.isCreativeMode) {
				p.getHeldItem().stackSize--;
				if (p.getHeldItem().stackSize <= 0) p.inventory.setInventorySlotContents(p.inventory.currentItem, (ItemStack) null);
			}
			return true;
		}
		if (riddenByEntity == null && p.username == tamer) setStay(!getStay());
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (lifeSpan >= 0 && ticksExisted > lifeSpan) attackEntityFrom(DamageSource.generic, getHealth());
		idleTicks++;
		if (getStay()) getNavigator().clearPathEntity();
		if ((isJumping || isAirBorne && motionY >= 0.2) && (Math.abs(motionX) + Math.abs(motionZ)) <= 0.2) {
			Vec3 vec = getLookVec();
			motionX = vec.xCoord / 4;
			motionZ = vec.zCoord / 4;
		}
	}

	@Override
	protected void updateAITick() {
		dataWatcher.updateObject(18, getHealth());
		dataWatcher.updateObject(21, new Byte(getAttackTarget() != null ? (byte) 1 : (byte) 0));
		dataWatcher.updateObject(23, new Byte((byte) (isInventoryEmpty() ? 0 : 1)));
	}

	public boolean filterMarker(AIFindMarker ai, int x, int y, int z) {
		return true;
	}

	public boolean isInventoryFull() {
		for (int a = 0; a < inv.length; a++) {
			if (inv[a] != null && inv[a].stackSize == 0) inv[a] = null;
			if (inv[a] == null || inv[a].stackSize == 0) return false;
		}

		return true;
	}

	public boolean isInventoryEmpty() {
		for (int a = 0; a < inv.length; a++) if (inv[a] != null) return false;

		return true;
	}

	public boolean doesInventoryHas(Class<? extends Item> c) {
		for (int a = 0; a < inv.length; a++) if (inv[a] != null && c.isInstance(inv[a].getItem())) return true;
		return false;
	}

	public boolean doesInventoryHas(ItemStack is) {
		for (int a = 0; a < inv.length; a++) {
			if (inv[a] != null) {
				ItemStack is2 = inv[a];
				if (is2.itemID == is.itemID && (is2.getItemDamage() == is.getItemDamage() || is.isItemStackDamageable())) return true;
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

	public void addItemToChest(ItemStack is, TileEntityChest chest) {
		if (is == null || is.stackSize <= 0 || chest == null) return;

		for (int a = 0; a < chest.getSizeInventory(); a++) {
			ItemStack is2 = chest.getStackInSlot(a);
			if (is2 == null || is2.stackSize <= 0) {
				chest.setInventorySlotContents(a, is.copy());
				decreaseItemStack(is);
				return;
			}
			if (is2.itemID == is.itemID && is2.getItemDamage() == is.getItemDamage()) {
				int amount = Math.min(is.stackSize, is2.getMaxStackSize() - is2.stackSize);
				ItemStack is3 = is.copy();
				is3.stackSize = amount;
				decreaseItemStack(is3);
				is.stackSize -= amount;
				is2.stackSize += amount;
				chest.setInventorySlotContents(a, is2);
			}
			if (is.stackSize <= 0) return;
		}
	}

	@Override
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
		super.setCurrentItemOrArmor(par1, par2ItemStack);
		if (par1 == 0) dataWatcher.updateObject(22, new Integer(getCurrentItemOrArmor(0) == null ? -1 : getCurrentItemOrArmor(0).itemID));
	}

	public ItemStack addItemStack(ItemStack is) {
		if (is == null || is.stackSize <= 0) return null;

		for (int a = 0; a < inv.length; a++) {
			if (inv[a] == null || inv[a].stackSize <= 0) {
				inv[a] = is;
				return null;
			}
			ItemStack is2 = inv[a];
			if (is2.itemID == is.itemID && is2.getItemDamage() == is.getItemDamage()) {
				int amount = Math.min(is.stackSize, is2.getMaxStackSize() - is2.stackSize);
				is.stackSize -= amount;
				is2.stackSize += amount;
				inv[a] = is2;
			}
			if (is.stackSize <= 0) return null;
		}
		return is;
	}

	public boolean decreaseItemStack(ItemStack is) {
		if (is == null || is.stackSize <= 0) return true;
		for (int a = 0; a < inv.length; a++) {
			ItemStack is2 = inv[a];
			if (is2 == null) continue;
			if (is2.itemID == is.itemID && (is2.getItemDamage() == is.getItemDamage() || !is.getItem().getHasSubtypes())) {
				int amount = Math.min(is.stackSize, is2.stackSize);
				is.stackSize -= amount;
				is2.stackSize -= amount;
				if (is2.stackSize <= 0) is2 = null;
				inv[a] = is2;
			}
			if (is.stackSize <= 0) return true;
		}
		return is.stackSize > 0;
	}

	public boolean shouldStoreItems(int a) {
		return idleTicks >= a;
	}

	public boolean shouldGoFindItemInChest() {
		return false;
	}

	public void resetIdle() {
		idleTicks = 0;
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	public boolean canExecuteAI() {
		return isAIEnabled() && !isDead && !getStay();
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	public void setMoveTo(Entity e, float ms) {
		setMoveTo(e.posX, e.posY, e.posZ, ms);
	}

	public void setMoveTo(double x, double y, double z, float ms) {
		Vec3 vec = Vec3.fakePool.getVecFromPool(x - posX, y - posY, z - posZ);
		double rate = 1;
		double maxLength = 15D;
		if (vec.lengthVector() > maxLength) rate = maxLength / vec.lengthVector();
		if (rate < 1) {
			vec.xCoord *= rate;
			vec.yCoord *= rate;
			vec.zCoord *= rate;
		}
		getNavigator().tryMoveToXYZ(posX + vec.xCoord, posY + vec.yCoord, posZ + vec.zCoord, ms);
	}

	public boolean attackEntityFrom(DamageSource d, int par2) {
		setStay(false);
		if (getAttackTarget() != null && d.getEntity() != null && d.getEntity() instanceof EntityLiving) {
			EntityLiving e = (EntityLiving) d.getEntity();
			if (!tamed) {
				setAttackTarget(e);
				setLastAttacker(e);
			}
		}
		return super.attackEntityFrom(d, par2);
	}

	@Override
	public void onDeath(DamageSource d) {
		super.onDeath(d);
		if (!this.worldObj.isRemote) {
			for (int a = 0; a < inv.length; a++) if (inv[a] != null) worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY + 0.3, posZ, inv[a]));
		} else for (int a = 0; a < rand.nextInt(10) + 10; a++) worldObj.spawnParticle("flame", posX + width / 2, posY + height / 2, posZ + width / 2, (rand.nextDouble() - 0.5) / 8, rand.nextDouble() * 0.2, (rand.nextDouble() - 0.5) / 8);
	}

	@Override
	protected void dropFewItems(boolean recentlyAttacked, int lootModify) {
		super.dropFewItems(recentlyAttacked, lootModify);

		if (rand.nextInt(7) <= (tamed ? 1 : 5)) dropItem(new ItemStack(CubeBots.cubePiece, new Random().nextInt(2) + 1));
		if (rand.nextInt(150) == 22) dropItem(new ItemStack(CubeBots.cubeBotCore));
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

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setString("Type", type.name());
		tag.setInteger("Max Health", maxHealth);
		tag.setBoolean("Tamed", tamed);
		tag.setString("Tamer", tamer == null || tamer.isEmpty() ? "-$" : tamer);
		tag.setInteger("Life Span", lifeSpan);
		tag.setBoolean("Stay", getStay());
		writeMarkedList(tag, "Marked Chests", markedChests);

		int[] itemIDs = new int[inv.length];
		int[] itemStackSizes = new int[inv.length];
		int[] itemDamages = new int[inv.length];
		for (int a = 0; a < inv.length; a++) {
			if (inv[a] == null) itemIDs[a] = itemStackSizes[a] = itemDamages[a] = 0; else {
				itemIDs[a] = inv[a].itemID;
				itemStackSizes[a] = inv[a].stackSize;
				itemDamages[a] = inv[a].getItemDamage();
			}
		}
		tag.setIntArray("INV IDs", itemIDs);
		tag.setIntArray("INV StackSizes", itemStackSizes);
		tag.setIntArray("INV Damages", itemDamages);
	}

	public void writeMarkedList(NBTTagCompound tag, String s, List<BlockCoord> list) {
		int[] array = new int[list.size() * 3];
		for (int a = 0; a < list.size(); a++) {
			BlockCoord b = list.get(a);
			array[a] = b.x;
			array[a + 1] = b.y;
			array[a + 2] = b.z;
		}
		tag.setIntArray(s, array);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		setType(EnumCubeBotType.valueOf(tag.getString("Type")));
		maxHealth = tag.getInteger("Max Health");
		tag.getBoolean("Tamed");
		tamer = tag.getString("Tamer").compareTo("-$") == 0 ? "" : tag.getString("Tamer");
		lifeSpan = tag.getInteger("Life Span");
		setStay(tag.getBoolean("Stay"));
		readMarkedList(tag, "Marked Chests", markedChests);

		int[] itemIDs = tag.getIntArray("INV IDs");
		int[] itemStackSizes = tag.getIntArray("INV StackSizes");
		int[] itemDamages = tag.getIntArray("INV Damages");
		inv = new ItemStack[itemIDs.length];
		for (int a = 0; a < itemIDs.length; a++) {
			int id = itemIDs[a];
			int size = a >= itemStackSizes.length ? 0 : itemStackSizes[a];
			int dmg = a >= itemDamages.length ? 0 : itemDamages[a];
			inv[a] = id != 0 ? new ItemStack(id, size, dmg) : null;
		}
	}

	public void readMarkedList(NBTTagCompound tag, String s, List list) {
		int[] array = tag.getIntArray(s);
		for (int a = 0; a < array.length / 3; a++) {
			BlockCoord bc = new BlockCoord(array[a * 3], array[a * 3 + 1], array[a * 3 + 2]);
			list.add(bc);
		}
	}
}
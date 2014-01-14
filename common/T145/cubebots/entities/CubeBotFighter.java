package T145.cubebots.entities;

import T145.cubebots.entities.ai.AIAttackTargetMelee;
import T145.cubebots.entities.ai.AIFindChestWithItem;
import T145.cubebots.entities.ai.AIFindTarget;
import T145.cubebots.entities.ai.AIGetItemFromChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class CubeBotFighter extends CubeBot {
	public CubeBotFighter(World par1World) {
		super(par1World);
		texture = "cubebots/textures/cubeBotFighter.png";
		setType(EnumCubeBotType.FIGHTER);
		setScale(100);
		maxHealth = 20;
		setHealth(getMaxHealth());

		tasks.addTask(3, new AIFindTarget(this, EntityMob.class, 16));
		tasks.addTask(3, new AIFindTarget(this, CubeBad.class, 16));
		tasks.addTask(3, new AIAttackTargetMelee(this, 0.25F, 1.2));
		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[] {ItemSword.class});
		tasks.addTask(4, new AIGetItemFromChest(find, 0.23F, new Object[] {ItemSword.class, new Integer[] { 1 }}));
		tasks.addTask(4, find);
	}

	@Override
	public boolean canPanic() {
		return false;
	}

	@Override
	public boolean shouldGoFindItemInChest() {
		return getCurrentItemOrArmor(0) == null;
	}

	@Override
	public int getAttackStrength(Entity e) {
		ItemStack is = getCurrentItemOrArmor(0);
		if (is != null) return (int) is.getItem().getDamageVsEntity(e, is);
		return 1;
	}
}
package T145.cubebots.entities;

import T145.cubebots.entities.ai.AIAttackTargetMelee;
import T145.cubebots.entities.ai.AIAttackTargetRanged;
import T145.cubebots.entities.ai.AIFindChestWithItem;
import T145.cubebots.entities.ai.AIFindTarget;
import T145.cubebots.entities.ai.AIGetItemFromChest;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CubeBotArcher extends CubeBot implements IRangedAttackMob
{
	public CubeBotArcher(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotArcher.png";
		setType(EnumCubeBotType.ARCHER);
		setScale(90);
		maxHealth = 20;
		setHealth(getMaxHealth());
		setCurrentItemOrArmor(0, new ItemStack(Item.bow));

		tasks.addTask(3, new AIFindTarget(this, EntityMob.class, 24));
		tasks.addTask(3, new AIFindTarget(this, CubeBad.class, 24));
		tasks.addTask(3, new AIAttackTargetRanged(this, 3, 18, 0.19F, 65));
		AIFindChestWithItem find = new AIFindChestWithItem(this, new ItemStack[]{new ItemStack(Item.arrow)});
		tasks.addTask(4, new AIGetItemFromChest(find, 0.23F, new Object[]{new ItemStack(Item.arrow), new Integer[]{0}}));
		tasks.addTask(4, find);
	}
	
	@Override
	public boolean canPanic()
	{
		return false;
	}
	
	@Override
	public boolean shouldGoFindItemInChest()
	{
		return !doesInventoryHas(new ItemStack(Item.arrow)) && !isInventoryFull();
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
		EntityArrow var2 = new EntityArrow(this.worldObj, this, entitylivingbase, 1.6F, 1F);
        int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
        int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());

        if (var3 > 0)
        {
            var2.setDamage(var2.getDamage() + (double)var3 * 0.5D + 0.5D);
        }

        if (var4 > 0)
        {
            var2.setKnockbackStrength(var4);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0)
        {
            var2.setFire(100);
        }

        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(var2);
        decreaseItemStack(new ItemStack(Item.arrow));
	}
}
package T145.cubebots;

import java.util.List;

import T145.cubebots.entities.CubeBot;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BehaviorDispenseCB implements IBehaviorDispenseItem {
	@Override
	public ItemStack dispense(IBlockSource bs, ItemStack is) {
		World world = bs.getWorld();
		if (!world.isRemote) {
			IPosition ip = BlockDispenser.getIPositionFromBlockSource(bs);
			List<CubeBot> list = world.getEntitiesWithinAABB(CubeBot.class, AxisAlignedBB.getBoundingBox(ip.getX(), ip.getY(), ip.getZ(), ip.getX(), ip.getY(), ip.getZ()).expand(1, 1, 1));
			if (!list.isEmpty()) {
				double x = bs.getX();
				double y = bs.getY();
				double z = bs.getZ();
				double dist = 999;
				CubeBot target = null;
				for (CubeBot c : list) {
					if (c.getDistanceSq(x, y, z) < dist) {
						dist = c.getDistanceSq(x, y, z);
						target = c;
					}
				}
				if (target != null) {
					if (is.getItem() == CubeBots.cubeBotCore && target.getHealth() < target.getMaxHealth()) {
						target.heal(10);
						playFX(bs, 2000);
						playFXAt(target, 2004);
						is.stackSize--;
					}
					if (is.getItem() == CubeBots.lifeCore && target.lifeSpan >= 0) {
						target.lifeSpan += 22000;
						playFX(bs, 2000);
						playFXAt(target, 2004);
						is.stackSize--;
					}
					if (is.getItem() == CubeBots.infiniteLifeCore && target.lifeSpan >= 0) {
						target.lifeSpan = -1;
						playFX(bs, 2000);
						playFXAt(target, 2004);
						is.stackSize--;
					}
					if (is.stackSize <= 0) is = null;
				}
			}
		}
		return is;
	}

	public static void playFX(IBlockSource bs, int id) {
		bs.getWorld().playAuxSFX(id, bs.getXInt(), bs.getYInt(), bs.getZInt(), 0);
	}

	public static void playFXAt(EntityLiving e, int id) {
		e.worldObj.playAuxSFX(id, (int) e.posX, (int) e.posY, (int) e.posZ, 0);
	}
}
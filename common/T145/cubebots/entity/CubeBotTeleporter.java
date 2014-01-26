package T145.cubebots.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CubeBotTeleporter extends CubeBot {
	public CubeBotTeleporter(World w) {
		super(w);
		setType(EnumCubeBotType.TELEPORTER);
	}

	@Override
	public boolean interact(EntityPlayer p) {
		if (p.getCurrentEquippedItem() != null && p.getCurrentEquippedItem().getItem() == Item.enderPearl) {
			List<Entity> l = p.worldObj.getLoadedEntityList();
			int index = -1;
			CubeBotTeleporter first = null;
			boolean teleported = false;
			for (int a = 0; a < l.size(); a++) {
				if (l.get(a) == this)
					index = a;
				if (l.get(a) instanceof CubeBotTeleporter && first == null) {
					first = (CubeBotTeleporter) l.get(a);
				}
				if (index >= 0 && a > index && l.get(a) instanceof CubeBotTeleporter) {
					p.setPosition(l.get(a).posX, l.get(a).posY, l.get(a).posZ);
					teleported = true;
					if (rand.nextInt(2) == 0) {
						if (!p.capabilities.isCreativeMode) {
							p.getHeldItem().stackSize--;
							if (p.getHeldItem().stackSize <= 0) {
								p.inventory.setInventorySlotContents(p.inventory.currentItem, (ItemStack) null);
							}
						}
					}
					break;
				}
			}
			if (!teleported) {
				p.setPosition(first.posX, first.posY, first.posZ);
				teleported = true;
				if (rand.nextInt(2) == 0) {
					if (!p.capabilities.isCreativeMode) {
						p.getHeldItem().stackSize--;
						if (p.getHeldItem().stackSize <= 0) {
							p.inventory.setInventorySlotContents(p.inventory.currentItem, (ItemStack) null);
						}
					}
				}
			}
			return true;
		}
		return super.interact(p);
	}
}
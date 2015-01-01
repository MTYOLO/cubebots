package com.willeze.cubebots.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CubeBotTeleport extends CubeBot {
	
	public CubeBotTeleport(World w) {
		super(w);
		setType(CubeBotEnum.TELEPORT);
	}

	@Override
	public boolean interact(EntityPlayer p) {
		if (p.getCurrentEquippedItem() != null && p.getCurrentEquippedItem().getItem() == Items.ender_pearl) {
			List<Entity> l = p.worldObj.getLoadedEntityList();
			int index = -1;
			CubeBotTeleport first = null;
			boolean teleported = false;
			for (int a = 0; a < l.size(); a++) {
				if (l.get(a) == this)
					index = a;
				if (l.get(a) instanceof CubeBotTeleport && first == null) {
					first = (CubeBotTeleport) l.get(a);
				}
				if (index >= 0 && a > index && l.get(a) instanceof CubeBotTeleport) {
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
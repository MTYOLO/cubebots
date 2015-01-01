package com.willeze.cubebots.item;

import java.util.List;

import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.entity.CubeBot;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemInstantCubeCall extends Item {

	public ItemInstantCubeCall() {
		super();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World worldObj, EntityPlayer e) {
		List<CubeBot> list = worldObj.getEntitiesWithinAABB(CubeBot.class, e.boundingBox.expand(32, 32, 32));
		if (list.isEmpty()) {
			if (!worldObj.isRemote)
				e.addChatComponentMessage(new ChatComponentText("No bots available"));
		} else {
			for (int a = 0; a < worldObj.getLoadedEntityList().size(); a++) {
				Entity ent = (Entity) worldObj.getLoadedEntityList().get(a);
				if (ent instanceof CubeBot) {
					CubeBot cb = (CubeBot) ent;
					double rate = 100;
					cb.setPosition(e.posX, e.posY, e.posZ);
					cb.getNavigator().clearPathEntity();
					cb.setVelocity(0, 0, 0);
					cb.fallDistance = 0;
				}
			}
		}
		return super.onItemRightClick(is, worldObj, e);
	}

}

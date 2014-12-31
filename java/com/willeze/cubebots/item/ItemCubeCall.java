package com.willeze.cubebots.item;

import ibxm.Player;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.entity.CubeBot;

public class ItemCubeCall extends Item {

	public ItemCubeCall() {
		super();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World worldObj, EntityPlayer e) {
		List<CubeBot> list = worldObj.getEntitiesWithinAABB(CubeBot.class, e.boundingBox.expand(32, 32, 32));
		if (list.isEmpty()) {
			if (!worldObj.isRemote)
				e.addChatComponentMessage(new ChatComponentText("No bots in range"));
		} else {
			for (int a = 0; a < list.size(); a++) {
				CubeBot cb = list.get(a);
				cb.setMoveTo(e, 0.50F);
			}
		}
		return super.onItemRightClick(is, worldObj, e);
	}

}

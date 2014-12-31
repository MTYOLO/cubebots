package com.willeze.cubebots.entity;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.ai.AICollect;
import com.willeze.cubebots.ai.AILumber;
import com.willeze.cubebots.ai.AIStoreItems;

public class CubeBotCollect extends CubeBot {

	public CubeBotCollect(World par1World) {
		super(par1World);
		setType(CubeBotEnum.COLLECT);
		
		this.tasks.addTask(3, new AICollect(this, 12, 0.45F));
		this.tasks.addTask(3, new AIStoreItems(this, 0.25F));
	}

}

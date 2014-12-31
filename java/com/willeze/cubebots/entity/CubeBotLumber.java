package com.willeze.cubebots.entity;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.ai.AICollect;
import com.willeze.cubebots.ai.AILumber;

public class CubeBotLumber extends CubeBot {

	public CubeBotLumber(World par1World) {
		super(par1World);
		setType(CubeBotEnum.LUMBER);
		this.tasks.addTask(3, new AILumber(this, 0.25F, 12D));
	}

}

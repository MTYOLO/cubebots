package com.willeze.cubebots.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.willeze.cubebots.CubeBots;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

public class RenderCubeBot extends RenderLiving {

	private static ResourceLocation textureLocation;

	public RenderCubeBot(ModelCubeBot modelCubeBot, float shadowSize) {
		super(modelCubeBot, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		if (par1Entity instanceof CubeBot) {
			textureLocation = new ResourceLocation(CubeBots.MODID + ":" + "textures/models/cubeBot.png");
		}
		if (par1Entity instanceof CubeBotCollect) {
			textureLocation = new ResourceLocation(CubeBots.MODID + ":" + "textures/models/cubeBotCollect.png");
		}
		if (par1Entity instanceof CubeBotLumber) {
			textureLocation = new ResourceLocation(CubeBots.MODID + ":" + "textures/models/cubeBotLumber.png");
		}
		if (par1Entity instanceof CubeBotMiner) {
			textureLocation = new ResourceLocation(CubeBots.MODID + ":" + "textures/models/cubeBotMiner.png");
		}
		if (par1Entity instanceof CubeBotTeleport) {
			textureLocation = new ResourceLocation(CubeBots.MODID + ":" + "textures/models/cubeBotTeleport.png");
		}
		return textureLocation;
	}

}
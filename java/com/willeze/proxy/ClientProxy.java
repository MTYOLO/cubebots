package com.willeze.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.entity.CubeBot;
import com.willeze.cubebots.entity.CubeBotCollect;
import com.willeze.cubebots.entity.CubeBotEnum;
import com.willeze.cubebots.entity.ModelCubeBot;
import com.willeze.cubebots.entity.RenderCubeBot;
import com.willeze.cubebots.tile.BlockRenderHandler;
import com.willeze.cubebots.tile.TileEntityMarker;
import com.willeze.cubebots.tile.TileEntityMarkerRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		RenderingRegistry.registerBlockHandler(new BlockRenderHandler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMarker.class, new TileEntityMarkerRenderer());
		RenderingRegistry.registerEntityRenderingHandler(CubeBot.class, new RenderCubeBot(new ModelCubeBot(), 0.3F));
	}

}
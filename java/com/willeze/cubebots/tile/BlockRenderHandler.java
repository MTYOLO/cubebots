package com.willeze.cubebots.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRenderHandler implements ISimpleBlockRenderingHandler {
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		World clientWorld = FMLClientHandler.instance().getClient().theWorld;
		if (block instanceof BlockMarker) {
			System.out.println("call me maybe");
//			TileEntityMarkerRenderer markerRender = TileEntityRenderer.getSpecialRendererForClass(TileEntityMarker.class);
//			markerRender.renderTileEntityAt(((BlockMarker) block).createInventoryTile(clientWorld), 0, 0, 0, 0);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return true;
	}

	@Override
	public int getRenderId() {
		return 0;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}
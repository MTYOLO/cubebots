package T145.cubebots.lib;

import T145.cubebots.block.BlockMarker;
import T145.cubebots.tileentity.TileEntityMarker;
import T145.cubebots.tileentity.TileEntityMarkerRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRenderHandler implements ISimpleBlockRenderingHandler {
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		World clientWorld = FMLClientHandler.instance().getClient().theWorld;
		if (block instanceof BlockMarker) {
			TileEntityMarkerRenderer markerRender = (TileEntityMarkerRenderer) TileEntityRenderer.instance.getSpecialRendererForClass(TileEntityMarker.class);
			markerRender.renderTileEntityAt(((BlockMarker) block).createInventoryTile(clientWorld), 0, 0, 0, 0);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return 384;
	}
}
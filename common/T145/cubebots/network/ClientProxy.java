package T145.cubebots.network;

import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.ModelCubeBot;
import T145.cubebots.entity.RenderCubeBot;
import T145.cubebots.lib.BlockRenderHandler;
import T145.cubebots.tileentity.TileEntityMarker;
import T145.cubebots.tileentity.TileEntityMarkerRenderer;
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
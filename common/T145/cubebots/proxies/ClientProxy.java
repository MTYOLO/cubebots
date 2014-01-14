package T145.cubebots.proxies;

import T145.cubebots.entities.CubeBot;
import T145.cubebots.entities.ModelCubeBot;
import T145.cubebots.entities.RenderCubeBot;
import T145.cubebots.handlers.BlockRenderHandler;
import T145.cubebots.objects.TileMarker;
import T145.cubebots.objects.TileMarkerRenderer;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		RenderingRegistry.registerBlockHandler(new BlockRenderHandler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMarker.class, new TileMarkerRenderer());
		RenderingRegistry.registerEntityRenderingHandler(CubeBot.class, new RenderCubeBot(new ModelCubeBot(), 0.3F));
	}
}
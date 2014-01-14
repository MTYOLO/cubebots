package T145.cubebots.objects;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileMarkerRenderer extends TileEntitySpecialRenderer {
	RenderBlocks rb = new RenderBlocks();

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		renderSignerAt((TileMarker) var1, var2, var4, var6, var8);
	}

	public void renderSignerAt(TileMarker signer, double x, double y, double z, float tick) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glTranslated(0.5, 0.05, 0.5);
		GL11.glScaled(-0.3 * signer.size, 0.3 * signer.size, -0.3 * signer.size);
		GL11.glTranslated(0, -0.25, 0);
		rb.renderBlockAsItem(Block.cloth, 0, 1F);
		GL11.glTranslated(0, 1, 0);

		if (tileEntityRenderer.entityLivingPlayer != null) GL11.glRotatef(tileEntityRenderer.entityLivingPlayer.ticksExisted, 0, 1.8F, 0);
		rb.renderBlockAsItem(Block.planks, 0, 1F);

		if (signer.renderBlock != null) {
			GL11.glTranslated(0, 1, 0);
			rb.renderBlockAsItem(signer.renderBlock, 0, 1F);
		}
		if (signer.renderEntity != null) {
			GL11.glTranslated(0, 0.5, 0);
			/*
			 * Render renderer = RenderManager.instance.getEntityRenderObject(signer.renderEntity);
			 * renderer.setRenderManager(RenderManager.instance);
			 * System.out.println(RenderManager.instance.renderEngine);
			 * if(renderer!=null) renderer.doRender(signer.renderEntity, 0, 0, 0, 0, tick);
			 */
			if (signer.inv) GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			RenderManager.instance.renderEntityWithPosYaw(signer.renderEntity, 0, 0, 0, 0, tick);
			if (signer.inv) {
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
			}
		}

		GL11.glPopMatrix();
	}
}
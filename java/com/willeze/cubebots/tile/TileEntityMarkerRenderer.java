package com.willeze.cubebots.tile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.willeze.cubebots.CubeBots;

import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityMarkerRenderer extends TileEntitySpecialRenderer {
	RenderBlocks rb = new RenderBlocks();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick) {
		renderSignerAt((TileEntityMarker) tileentity, x, y, z, tick);
	}

	public void renderSignerAt(TileEntityMarker marker, double x, double y, double z, float tick) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.07F, (float) z + 0.5F);

		GL11.glScaled(-0.3 * marker.size, 0.3 * marker.size, -0.3 * marker.size);
		GL11.glTranslated(0, 0.2F, 0);
		rb.renderBlockAsItem(Blocks.bookshelf, 0, 1F);

		GL11.glRotatef((float) tick * marker.size, 0.0F, 0.0F, 1.0F);
		System.out.println(tick * marker.size);

		GL11.glTranslated(0, 1, 0);
		GL11.glRotatef(tick, 0, 2F, 0);
		rb.renderBlockAsItem(Blocks.planks, 0, 1F);

		if (marker.renderBlock != null) {
			GL11.glTranslated(0, 1, 0);
			rb.renderBlockAsItem(marker.renderBlock, 0, 1F);
			GL11.glTranslated(0, 0.5, 0);

			if (marker.inv) {
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);
				RenderManager.instance.renderEntityWithPosYaw(marker.renderEntity, x, y, z, 2f, tick);
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
			}
		}

		GL11.glPopMatrix();
	}
}
/*package T145.cubebots.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderCubeBot extends RenderLiving
{
	float scale;
	public RenderCubeBot(ModelBase par1ModelBase, float par2)
	{
		super(par1ModelBase, par2);
	}
	
	@Override
	protected void preRenderCallback(EntityLiving e, float par2)
	{
		super.preRenderCallback(e, par2);
		GL11.glScalef(((CubeBot)e).getScale(),((CubeBot)e).getScale(),((CubeBot)e).getScale());
	}
	
	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		renderCubeBot((CubeBot) par1Entity, par2, par4, par6, par8, par9);
	}
	
	public void renderCubeBot(CubeBot cb, double d1, double d2, double d3, float f1, float f2)
	{
		GL11.glPushMatrix();
		if(cb.getStay())
		{
			GL11.glTranslated(0, -0.4*cb.getScale(), 0);
		}
		super.doRender(cb, d1, d2, d3, f1, f2);
		
		renderLivingAt(cb, d1, d2, d3);
        float f3 = interpolateRotation(cb.prevRenderYawOffset, cb.renderYawOffset, f2);
        float f4 = handleRotationFloat(cb, f2);
		rotateCorpse(cb, f4, f3, f2);
		preRenderCallback(cb, f2);
		doAdditionalRender(cb, d1, d2, d3, f1, f2);
		GL11.glPopMatrix();
	}

	public void doAdditionalRender(CubeBot cb, double d1, double d2, double d3, float f1, float f2)
	{
		if(cb.getType() == EnumCubeBotType.COLLECTOR)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(-0.2, 0.8, -0.2);
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			
			TileEntityChestRenderer chestRender = (TileEntityChestRenderer) TileEntityRenderer.instance.getSpecialRendererForClass(TileEntityChest.class);
			TileEntityChest chest = new TileEntityChest();
			chest.adjacentChestChecked = true;
			chest.adjacentChestXNeg = chest.adjacentChestXPos = chest.adjacentChestZNeg = chest.adjacentChestZPosition = null;
			byte chestEmpty = cb.getDataWatcher().getWatchableObjectByte(23);
			float lid = chestEmpty;
			chest.lidAngle = lid;
			chestRender.renderTileEntityChestAt(chest, 0, 0, 0, 1);
		}
		
		if(cb.getType() == EnumCubeBotType.FIGHTER || cb.getType()==EnumCubeBotType.ARCHER || cb.getType() == EnumCubeBotType.LUMBER || cb.getType() == EnumCubeBotType.MINER || cb.getType() == EnumCubeBotType.GRASSER)
		{
			boolean isAttacker = cb.getType()==EnumCubeBotType.ARCHER || cb.getType()==EnumCubeBotType.FIGHTER;
			byte hasTarget = cb.getDataWatcher().getWatchableObjectByte(21);
			int itemID = cb.getDataWatcher().getWatchableObjectInt(22);
			if(itemID==-1) itemID = cb.getType() == EnumCubeBotType.LUMBER ? Item.axeWood.itemID : cb.getType() == EnumCubeBotType.MINER ? Item.pickaxeWood.itemID : cb.getType() == EnumCubeBotType.GRASSER ? Item.shears.itemID : Item.swordWood.itemID;
			ItemStack is = new ItemStack(itemID,1,0);
			if((isAttacker && hasTarget!=0) ^ cb.getDataWatcher().getWatchableObjectInt(22)>0) 
				is.addEnchantment(Enchantment.power, 10);
			
			double translateHor = 0.2;
			double translateVert = 0.25;
			if((isAttacker && hasTarget!=0) ^ cb.getDataWatcher().getWatchableObjectInt(22)>0) translateVert=0;
			if(is.getItem() instanceof ItemSword || is.getItem() instanceof ItemBow || is.getItem() instanceof ItemTool) translateHor = 0.25;
			GL11.glTranslated(-translateHor, 0.9 - translateVert, translateHor);
			GL11.glRotatef(90, -1, 0, 0);
			GL11.glScaled(1, 1, (translateVert!=0 ? 1.5 : 3));
			renderItem(is, cb);
		}
		
		if(cb.getType() == EnumCubeBotType.FIXER)
		{
			if(renderManager.livingPlayer.ticksExisted%4==0 && cb.getDataWatcher().getWatchableObjectByte(23)!=0)
			{
				double x = (cb.boundingBox.minX + cb.boundingBox.maxX)/2;
				double y = cb.boundingBox.maxY;
				double z = (cb.boundingBox.minZ + cb.boundingBox.maxZ)/2;
				EntityHeartFX fx = new EntityHeartFX(cb.worldObj, x, y, z, 0, 0.3, 0, 0.8F);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
			}
		}

		if(cb.getType() == EnumCubeBotType.BREEDER)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			renderManager.renderEntityWithPosYaw(new CubeBot(cb.worldObj), 0, 0, 0, 0, f2);
		}
		
		if(cb.getType() == EnumCubeBotType.MILKER)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.23F, 0.23F, 0.23F);
			GL11.glRotatef((float)Minecraft.getSystemTime()/6F, 0, 1, 0);
			renderManager.renderEntityWithPosYaw(new EntityCow(cb.worldObj), 0, 0, 0, 0, f2);
		}
		
		if(cb.getType() == EnumCubeBotType.BUTCHER)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.23F, 0.23F, 0.23F);
			GL11.glRotatef((float)Minecraft.getSystemTime()/6F, 0, 1, 0);
			renderManager.renderEntityWithPosYaw(new EntityPig(cb.worldObj), 0, 0, 0, 0, f2);
		}
		
		if(cb.getType() == EnumCubeBotType.SMITHY)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(-0.2, 0.8, -0.2);
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			renderBlocks.renderBlockAnvilOrient((BlockAnvil)Block.anvil, 0, 0, 0, 0, true);
		}
		
		if(cb.getType() == EnumCubeBotType.OREFINDER)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 1.3, 0);
			GL11.glScalef(0.3F, 0.3F, 0.3F);
			
			float time = Minecraft.getSystemTime();
			GL11.glRotatef((float)time/6.4F, 0, 1, 0);
			GL11.glTranslated(0, Math.sin(time/100D)/2D, 0);
			
			EntityItem i = new EntityItem(cb.worldObj);
			Block block = Block.oreCoal;
			float modTime = (time/5000)%6F;
			if(modTime>1) block = Block.oreIron;
			if(modTime>2) block = Block.oreGold;
			if(modTime>3) block = Block.oreDiamond;
			if(modTime>4) block = Block.oreLapis;
			if(modTime>5) block = Block.oreRedstone;
			
			renderBlocks.renderBlockAsItem(block, 1, 1);
		}
		

		if(cb.getType() == EnumCubeBotType.TELEPORTER)
		{
			if(renderManager.livingPlayer.ticksExisted%4==0)
			{
				double x = (cb.boundingBox.minX + cb.boundingBox.maxX)/2;
				double y = cb.boundingBox.maxY;
				double z = (cb.boundingBox.minZ + cb.boundingBox.maxZ)/2;
				cb.worldObj.spawnParticle("portal", x, y, z, (new Random().nextDouble()-0.5)/3, 0, (new Random().nextDouble()-0.5)/3);
			}
		}
		
		if(cb.getType() == EnumCubeBotType.SIREN)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.35F, 0.35F, 0.35F);
			GL11.glRotatef((float)Minecraft.getSystemTime()/6F, 0, 1, 0);
			boolean sirening = cb.getDataWatcher().getWatchableObjectInt(25)!=0;
			Entity e = sirening ? new EntityZombie(cb.worldObj) : new EntityVillager(cb.worldObj);
			renderManager.renderEntityWithPosYaw(e, 0, 0, 0, 0, f2);
		}
		
		if(cb.getType() == EnumCubeBotType.TAMER)
		{
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			GL11.glRotatef((float)Minecraft.getSystemTime()/6F, 0, 1, 0);
			Entity e = new EntityWolf(cb.worldObj);
			int rnd = new Random().nextInt(30)+200;
			if(renderManager.livingPlayer.ticksExisted%rnd > rnd/2) e = new EntityOcelot(cb.worldObj);
			renderManager.renderEntityWithPosYaw(e, 0, 0, 0, 0, f2);
		}
	}
	
	public void renderItem(ItemStack is, EntityLiving cb)
	{
		RenderEngine re = renderManager.renderEngine;
		EntityItem ei = new EntityItem(cb.worldObj);
		ei.setEntityItemStack(is);
		ForgeHooksClient.renderEntityItem(ei, is, 0, 0, new Random(), re, renderBlocks);
		ItemRenderer ire = RenderManager.instance.itemRenderer;

        Tessellator var5 = Tessellator.instance;
        int var6 = 	is.getIconIndex();
        float var7 = ((float)(var6 % 16 * 16) + 0.0F) / 256.0F;
        float var8 = ((float)(var6 % 16 * 16) + 15.99F) / 256.0F;
        float var9 = ((float)(var6 / 16 * 16) + 0.0F) / 256.0F;
        float var10 = ((float)(var6 / 16 * 16) + 15.99F) / 256.0F;
        float var11 = 0.0F;
        float var12 = 0.3F;
        float var13 = 0.5F;
        GL11.glScalef(var13, var13, var13);
        renderManager.itemRenderer.renderItemIn2D(var5, var8, var9, var7, var10, 0.0625F);

        if (is.hasEffect())
        {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            renderManager.renderEngine.bindTexture(renderManager.renderEngine.getTexture("%blur%/misc/glint.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float var14 = 0.76F;
            GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float var15 = 0.125F;
            GL11.glScalef(var15, var15, var15);
            float var16 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(var16, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            renderManager.itemRenderer.renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(var15, var15, var15);
            var16 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-var16, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            renderManager.itemRenderer.renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
	}
	
    private float interpolateRotation(float par1, float par2, float par3)
    {
        float var4;

        for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return par1 + par3 * var4;
    }

}
 */

package T145.cubebots.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCubeBot extends RenderLiving {
	public ResourceLocation texture;

	public RenderCubeBot(ModelBase m, ResourceLocation r) {
		super(m, 0.5F);
		this.texture = r;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

	private float scale;

	public RenderCubeBot(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	protected void preRenderCallback(EntityLiving e, float par2) {
		super.preRenderCallback(e, par2);
		GL11.glScalef(((CubeBot) e).getScale(), ((CubeBot) e).getScale(),
				((CubeBot) e).getScale());
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		renderCubeBot((CubeBot) par1Entity, par2, par4, par6, par8, par9);
	}

	public void renderCubeBot(CubeBot cb, double d1, double d2, double d3, float f1, float f2) {
		GL11.glPushMatrix();
		if (cb.getStay()) {
			GL11.glTranslated(0, -0.4 * cb.getScale(), 0);
		}
		super.doRender(cb, d1, d2, d3, f1, f2);

		renderLivingAt(cb, d1, d2, d3);
		float f3 = this.interpolateRotation(cb.prevRenderYawOffset, cb.renderYawOffset, f2);
		float f4 = handleRotationFloat(cb, f2);
		rotateCorpse(cb, f4, f3, f2);
		preRenderCallback(cb, f2);
		doAdditionalRender(cb, d1, d2, d3, f1, f2);
		GL11.glPopMatrix();
	}

	public void doAdditionalRender(CubeBot cb, double d1, double d2, double d3, float f1, float f2) {
		if (cb.getType() == EnumCubeBotType.COLLECTOR) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(-0.2, 0.8, -0.2);
			GL11.glScalef(0.4F, 0.4F, 0.4F);

			TileEntityChestRenderer chestRender = (TileEntityChestRenderer) TileEntityRenderer.instance.getSpecialRendererForClass(TileEntityChest.class);
			TileEntityChest chest = new TileEntityChest();
			chest.adjacentChestChecked = true;
			chest.adjacentChestXNeg = chest.adjacentChestXPos = chest.adjacentChestZNeg = chest.adjacentChestZPosition = null;
			byte chestEmpty = cb.getDataWatcher().getWatchableObjectByte(23);
			float lid = chestEmpty;
			chest.lidAngle = lid;
			chestRender.renderTileEntityChestAt(chest, 0, 0, 0, 1);
		}

		if (cb.getType() == EnumCubeBotType.FIGHTER || cb.getType() == EnumCubeBotType.ARCHER || cb.getType() == EnumCubeBotType.LUMBER || cb.getType() == EnumCubeBotType.MINER || cb.getType() == EnumCubeBotType.GRASSER) {
			boolean isAttacker = cb.getType() == EnumCubeBotType.ARCHER
					|| cb.getType() == EnumCubeBotType.FIGHTER;
			byte hasTarget = cb.getDataWatcher().getWatchableObjectByte(21);
			int itemID = cb.getDataWatcher().getWatchableObjectInt(22);
			if (itemID == -1) itemID = cb.getType() == EnumCubeBotType.LUMBER ? Item.axeWood.itemID : cb.getType() == EnumCubeBotType.MINER ? Item.pickaxeWood.itemID : cb.getType() == EnumCubeBotType.GRASSER ? Item.shears.itemID : Item.swordWood.itemID;
			ItemStack is = new ItemStack(itemID, 1, 0);
			if ((isAttacker && hasTarget != 0) ^ cb.getDataWatcher().getWatchableObjectInt(22) > 0) is.addEnchantment(Enchantment.power, 10);

			double translateHor = 0.2;
			double translateVert = 0.25;
			if ((isAttacker && hasTarget != 0) ^ cb.getDataWatcher().getWatchableObjectInt(22) > 0) translateVert = 0;
			if (is.getItem() instanceof ItemSword || is.getItem() instanceof ItemBow || is.getItem() instanceof ItemTool) translateHor = 0.25;
			GL11.glTranslated(-translateHor, 0.9 - translateVert, translateHor);
			GL11.glRotatef(90, -1, 0, 0);
			GL11.glScaled(1, 1, (translateVert != 0 ? 1.5 : 3));
			renderItem(is, cb);
		}

		if (cb.getType() == EnumCubeBotType.FIXER) {
			if (renderManager.livingPlayer.ticksExisted % 4 == 0 && cb.getDataWatcher().getWatchableObjectByte(23) != 0) {
				double x = (cb.boundingBox.minX + cb.boundingBox.maxX) / 2;
				double y = cb.boundingBox.maxY;
				double z = (cb.boundingBox.minZ + cb.boundingBox.maxZ) / 2;
				EntityHeartFX fx = new EntityHeartFX(cb.worldObj, x, y, z, 0, 0.3, 0, 0.8F);
				FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
			}
		}

		if (cb.getType() == EnumCubeBotType.BREEDER) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			renderManager.renderEntityWithPosYaw(new CubeBot(cb.worldObj), 0, 0, 0, 0, f2);
		}

		if (cb.getType() == EnumCubeBotType.MILKER) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.23F, 0.23F, 0.23F);
			GL11.glRotatef(Minecraft.getSystemTime() / 6F, 0, 1, 0);
			renderManager.renderEntityWithPosYaw(new EntityCow(cb.worldObj), 0, 0, 0, 0, f2);
		}

		if (cb.getType() == EnumCubeBotType.BUTCHER) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.23F, 0.23F, 0.23F);
			GL11.glRotatef(Minecraft.getSystemTime() / 6F, 0, 1, 0);
			renderManager.renderEntityWithPosYaw(new EntityPig(cb.worldObj), 0, 0, 0, 0, f2);
		}

		if (cb.getType() == EnumCubeBotType.SMITHY) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(-0.2, 0.8, -0.2);
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			renderBlocks.renderBlockAnvilOrient((BlockAnvil) Block.anvil, 0, 0, 0, 0, true);
		}

		if (cb.getType() == EnumCubeBotType.OREFINDER) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 1.3, 0);
			GL11.glScalef(0.3F, 0.3F, 0.3F);

			float time = Minecraft.getSystemTime();
			GL11.glRotatef(time / 6.4F, 0, 1, 0);
			GL11.glTranslated(0, Math.sin(time / 100D) / 2D, 0);

			EntityItem i = new EntityItem(cb.worldObj);
			Block block = Block.oreCoal;
			float modTime = (time / 5000) % 6F;
			if (modTime > 1) block = Block.oreIron;
			if (modTime > 2) block = Block.oreGold;
			if (modTime > 3) block = Block.oreDiamond;
			if (modTime > 4) block = Block.oreLapis;
			if (modTime > 5) block = Block.oreRedstone;

			renderBlocks.renderBlockAsItem(block, 1, 1);
		}

		if (cb.getType() == EnumCubeBotType.TELEPORTER) {
			if (renderManager.livingPlayer.ticksExisted % 4 == 0) {
				double x = (cb.boundingBox.minX + cb.boundingBox.maxX) / 2;
				double y = cb.boundingBox.maxY;
				double z = (cb.boundingBox.minZ + cb.boundingBox.maxZ) / 2;
				cb.worldObj.spawnParticle("portal", x, y, z, (new Random().nextDouble() - 0.5) / 3, 0, (new Random().nextDouble() - 0.5) / 3);
			}
		}

		if (cb.getType() == EnumCubeBotType.SIREN) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.35F, 0.35F, 0.35F);
			GL11.glRotatef(Minecraft.getSystemTime() / 6F, 0, 1, 0);
			boolean sirening = cb.getDataWatcher().getWatchableObjectInt(25) != 0;
			Entity e = sirening ? new EntityZombie(cb.worldObj) : new EntityVillager(cb.worldObj);
			renderManager.renderEntityWithPosYaw(e, 0, 0, 0, 0, f2);
		}

		if (cb.getType() == EnumCubeBotType.TAMER) {
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glTranslated(0, 0.8, 0);
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			GL11.glRotatef(Minecraft.getSystemTime() / 6F, 0, 1, 0);
			Entity e = new EntityWolf(cb.worldObj);
			int rnd = new Random().nextInt(30) + 200;
			if (renderManager.livingPlayer.ticksExisted % rnd > rnd / 2) e = new EntityOcelot(cb.worldObj);
			renderManager.renderEntityWithPosYaw(e, 0, 0, 0, 0, f2);
		}
	}

	private void renderItem(ItemStack is, CubeBot cb) {
		TextureManager re = renderManager.renderEngine;
		EntityItem ei = new EntityItem(cb.worldObj);
		ei.setEntityItemStack(is);
		ForgeHooksClient.renderEntityItem(ei, is, 0, 0, new Random(), re, renderBlocks);
		ItemRenderer ire = RenderManager.instance.itemRenderer;

		Tessellator var5 = Tessellator.instance;
		int var6 = is.getIconIndex().toString().length();
		float var7 = (var6 % 16 * 16 + 0.0F) / 256.0F;
		float var8 = (var6 % 16 * 16 + 15.99F) / 256.0F;
		float var9 = (var6 / 16 * 16 + 0.0F) / 256.0F;
		float var10 = (var6 / 16 * 16 + 15.99F) / 256.0F;
		float var11 = 0.0F;
		float var12 = 0.3F;
		float var13 = 0.5F;
		GL11.glScalef(var13, var13, var13);
		ItemRenderer.renderItemIn2D(var5, var8, var9, var7, var10, var6, var6, 0.0625F);

		if (is.hasEffect()) {
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_LIGHTING);
			renderManager.renderEngine.bindTexture((ResourceLocation) renderManager.renderEngine.getTexture(new ResourceLocation("%blur%/misc/glint.png")));
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
			float var14 = 0.76F;
			GL11.glColor4f(0.5F * var14, 0.25F * var14, 0.8F * var14, 1.0F);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glPushMatrix();
			float var15 = 0.125F;
			GL11.glScalef(var15, var15, var15);
			float var16 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
			GL11.glTranslatef(var16, 0.0F, 0.0F);
			GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
			ItemRenderer.renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, var6, var6, 0.0625F);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(var15, var15, var15);
			var16 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
			GL11.glTranslatef(-var16, 0.0F, 0.0F);
			GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
			ItemRenderer.renderItemIn2D(var5, 0.0F, 0.0F, 1.0F, 1.0F, var6, var6, 0.0625F);
			GL11.glPopMatrix();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}
	}

	private float interpolateRotation(float par1, float par2, float par3) {
		float var4;

		for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F) {}

		while (var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		return par1 + par3 * var4;
	}
}
package T145.cubebots.entity;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCubeBot extends ModelBase
{
	
    ModelRenderer body;
    ModelRenderer leftLeg;
    ModelRenderer rightLeg;
    
    public ModelCubeBot()
  	{
    	textureWidth = 64;
    	textureHeight = 32;
    
    	body = new ModelRenderer(this, 0, 0);
        body.addBox(-6F, 11F, -6F, 12, 12, 12);
        body.setRotationPoint(0F, 0F, 0F);
        body.setTextureSize(64, 32);
        body.mirror = true;
        leftLeg = new ModelRenderer(this, 0, 29);
        leftLeg.addBox(2F, 0F, -1F, 2, 1, 2);
        leftLeg.setRotationPoint(0F, 23F, 0F);
        leftLeg.setTextureSize(64, 32);
        leftLeg.mirror = true;
        rightLeg = new ModelRenderer(this, 8, 29);
        rightLeg.addBox(-4F, 0F, -1F, 2, 1, 2);
        rightLeg.setRotationPoint(0F, 23F, 0F);
        rightLeg.setTextureSize(64, 32);
        rightLeg.mirror = true;
  	}	
  
  	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  	{
	    super.render(entity, f, f1, f2, f3, f4, f5);
	    rotateStuffs((CubeBot) entity, f, f1, f2, f3, f4, f5);
	    body.render(f5);
	    leftLeg.render(f5);
	    rightLeg.render(f5);
  	}
  	
  	public void rotateStuffs(CubeBot cb, float f1, float f2, float f3, float f4, float f5, float f6)
  	{
	    setRotationAngles(f1, f2, f3, f4, f5, f6, cb);
        this.rightLeg.rotateAngleX = MathHelper.cos(f1 * 0.6662F) * 1.4F * f2;
        this.leftLeg.rotateAngleX = MathHelper.cos(f1 * 0.6662F + (float)Math.PI) * 1.4F * f2;
        this.rightLeg.rotateAngleY = 0.0F;
        this.leftLeg.rotateAngleY = 0.0F;

  	}
}

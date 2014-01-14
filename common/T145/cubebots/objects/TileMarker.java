package T145.cubebots.objects;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileMarker extends TileEntity
{
	Block renderBlock;
	Entity renderEntity;
	public float size;
	boolean inv;
	
	public TileMarker()
	{
	}
	
	public TileMarker(Block b, Entity e, float s)
	{
		renderBlock = b;
		renderEntity = e;
		size = s;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("Block", renderBlock!=null ? renderBlock.blockID : -1);
		tag.setString("Entity", renderEntity!=null ? EntityList.getEntityString(renderEntity) : "NANANAN");
		tag.setFloat("Size", size);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		renderBlock = tag.getInteger("Block")>0 ? Block.blocksList[tag.getInteger("Block")] : null;
		renderEntity = (Entity) (tag.getString("Entity").compareTo("NANANAN")!=0  && tag.getString("Entity").length()>0? 
				EntityList.createEntityByName(tag.getString("Entity"), worldObj) : null);
		size = tag.getFloat("Size");
	}
}

package T145.cubebots.entities;

import java.util.ArrayList;
import java.util.List;

import T145.cubebots.CubeBots;
import T145.cubebots.entities.ai.AIBringChicken;
import T145.cubebots.entities.ai.AIFindChestWithItem;
import T145.cubebots.entities.ai.AIFindMarker;
import T145.cubebots.entities.ai.AIGetItemFromChest;
import T145.cubebots.entities.ai.AIMilkCows;
import T145.cubebots.entities.ai.AIStoreItems;
import T145.cubebots.world.BlockCoord;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CubeBotChickBring extends CubeBot
{
	public List<BlockCoord> markedChick = new ArrayList();

	public CubeBotChickBring(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotChickBring.png";
		setType(EnumCubeBotType.CHICKBRING);
		setScale(80);

		tasks.addTask(1, new AIFindMarker(this, markedChick, 8, 10));
		tasks.addTask(3, new AIBringChicken(this, 32, 0.3F, 12));
	}

	@Override
	public boolean canBeSteered()
	{
		return false;
	}
	
	@Override
	public boolean filterMarker(AIFindMarker ai, int x, int y, int z)
	{
		if(Block.blocksList[worldObj.getBlockId(x, y, z)] == CubeBots.markerChicken) return true;
		return false;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		writeMarkedList(tag, "Marked Chick", markedChick);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		readMarkedList(tag, "Marked Chick", markedChick);
	}
}

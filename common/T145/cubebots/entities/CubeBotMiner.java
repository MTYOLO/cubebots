package T145.cubebots.entities;

import java.util.ArrayList;
import java.util.List;

import T145.cubebots.CubeBots;
import T145.cubebots.entities.ai.AIFindChestWithItem;
import T145.cubebots.entities.ai.AIFindMarker;
import T145.cubebots.entities.ai.AIGetItemFromChest;
import T145.cubebots.entities.ai.AIMiner;
import T145.cubebots.world.BlockCoord;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CubeBotMiner extends CubeBot
{
	public List<BlockCoord> markedOre = new ArrayList();

	public CubeBotMiner(World par1World)
	{
		super(par1World);
		texture = "cubebots/textures/cubeBotMiner.png";
		setType(EnumCubeBotType.MINER);

		tasks.addTask(1, new AIFindMarker(this, markedOre, 8, 10));
		AIFindChestWithItem find = new AIFindChestWithItem(this, new Class[]{ItemPickaxe.class});
		tasks.addTask(3, find);
		tasks.addTask(3, new AIGetItemFromChest(find, 0.23F, new Object[]{ItemPickaxe.class, new Integer[]{1}}));
		tasks.addTask(4, new AIMiner(this, 0.25F, 3D));
	}

	@Override
	public boolean shouldGoFindItemInChest()
	{
		return getCurrentItemOrArmor(0) == null;
	}
	
	@Override
	public boolean filterMarker(AIFindMarker ai, int x, int y, int z)
	{
		if(Block.blocksList[worldObj.getBlockId(x, y, z)] == CubeBots.markerOre) return true;
		return false;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		writeMarkedList(tag, "Marked Ore", markedOre);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		readMarkedList(tag, "Marked Ore", markedOre);
	}
}

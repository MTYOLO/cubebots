package T145.cubebots.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AIFindChestWithItem;
import T145.cubebots.entity.ai.AIFindMarker;
import T145.cubebots.entity.ai.AIGetItemFromChest;
import T145.cubebots.entity.ai.AITameOcelots;
import T145.cubebots.entity.ai.AITameWolves;
import T145.cubebots.world.BlockCoord;

public class CubeBotTamer extends CubeBot {
	public List<BlockCoord> markedWolf = new ArrayList();
	public List<BlockCoord> markedCat = new ArrayList();

	public CubeBotTamer(World w) {
		super(w);
		setType(EnumCubeBotType.TAMER);

		tasks.addTask(1, new AIFindMarker(this, markedWolf, 8, 10));
		tasks.addTask(1, new AIFindMarker(this, markedCat, 8, 10));
		tasks.addTask(3, new AITameWolves(this, 16, 0.24F));
		tasks.addTask(3, new AITameOcelots(this, 16, 0.24F));
		AIFindChestWithItem find = new AIFindChestWithItem(this, new ItemStack[] { new ItemStack(Item.bone), new ItemStack(Item.fishRaw) });
		tasks.addTask(5, new AIGetItemFromChest(find, 0.23F, new Object[] { new ItemStack(Item.bone), new Integer[] { 0 }, new ItemStack(Item.fishRaw), new Integer[] { 0 } }));
		tasks.addTask(5, find);
	}

	@Override
	public boolean shouldGoFindItemInChest() {
		return !doesInventoryHas(new ItemStack(Item.bone)) && !isInventoryFull();
	}

	@Override
	public boolean filterMarker(AIFindMarker ai, int x, int y, int z) {
		if (ai != null && ai.list == markedWolf && Block.blocksList[worldObj.getBlockId(x, y, z)] == CubeBots.markerWolf)
			return true;
		if (ai != null && ai.list == markedCat && Block.blocksList[worldObj.getBlockId(x, y, z)] == CubeBots.markerCat)
			return true;
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		writeMarkedList(tag, "Marked Wolf", markedWolf);
		writeMarkedList(tag, "Marked Cat", markedCat);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		readMarkedList(tag, "Marked Wolf", markedWolf);
		readMarkedList(tag, "Marked Cat", markedCat);
	}
}
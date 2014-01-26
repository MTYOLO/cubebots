package T145.cubebots.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AIButcher;
import T145.cubebots.entity.ai.AIFindMarker;
import T145.cubebots.entity.ai.AIStoreItems;
import T145.cubebots.world.BlockCoord;

public class CubeBotButcher extends CubeBot {
	public List<BlockCoord> markedPig = new ArrayList();

	public CubeBotButcher(World w) {
		super(w);
		setType(EnumCubeBotType.BUTCHER);

		tasks.addTask(1, new AIFindMarker(this, markedPig, 8, 10));
		tasks.addTask(3, new AIButcher(this, 16, 0.23F, 16F));
		tasks.addTask(4, new AIStoreItems(this, 0.23F));
	}

	@Override
	public boolean filterMarker(AIFindMarker ai, int x, int y, int z) {
		if (Block.blocksList[worldObj.getBlockId(x, y, z)] == CubeBots.markerPig)
			return true;
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		writeMarkedList(tag, "Marked Pig", markedPig);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		readMarkedList(tag, "Marked Pig", markedPig);
	}
}
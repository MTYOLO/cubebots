package T145.cubebots.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import T145.cubebots.CubeBots;
import T145.cubebots.entity.ai.AIBringChicken;
import T145.cubebots.entity.ai.AIFindMarker;
import T145.cubebots.world.BlockCoord;

public class CubeBotChickBring extends CubeBot {
	public List<BlockCoord> markedChick = new ArrayList();

	public CubeBotChickBring(World w) {
		super(w);
		setType(EnumCubeBotType.CHICKBRING);
		setScale(80);

		tasks.addTask(1, new AIFindMarker(this, markedChick, 8, 10));
		tasks.addTask(3, new AIBringChicken(this, 32, 0.3F, 12));
	}

	@Override
	public boolean canBeSteered() {
		return false;
	}

	@Override
	public boolean filterMarker(AIFindMarker ai, int x, int y, int z) {
		if (Block.blocksList[worldObj.getBlockId(x, y, z)] == CubeBots.markerChicken)
			return true;
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		writeMarkedList(tag, "Marked Chick", markedChick);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		readMarkedList(tag, "Marked Chick", markedChick);
	}
}
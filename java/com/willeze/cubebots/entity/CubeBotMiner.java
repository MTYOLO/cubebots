package com.willeze.cubebots.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.willeze.cubebots.BlockCoord;
import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.ai.AIFindMarker;
import com.willeze.cubebots.ai.AIMiner;

public class CubeBotMiner extends CubeBot {
	public List<BlockCoord> markedOre = new ArrayList();

	public CubeBotMiner(World par1World) {
		super(par1World);
		setType(CubeBotEnum.MINER);

		this.tasks.addTask(1, new AIFindMarker(this, markedOre, 8, 10));
		this.tasks.addTask(3, new AIMiner(this, 0.25F, 3D));
	}

	@Override
	public boolean filterMarker(AIFindMarker ai, int x, int y, int z) {
		if (worldObj.getBlock(x, y, z) == CubeBots.blockOreMarker)
			return true;
		return false;
	}
}
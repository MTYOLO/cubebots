package com.willeze.cubebots.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMarker extends TileEntity {
	public Block renderBlock;
	public Entity renderEntity;
	public float size;
	public boolean inv;

	public TileEntityMarker(Block b, Entity e, float s) {
		renderBlock = b;
		renderEntity = e;
		size = s;
	}
}

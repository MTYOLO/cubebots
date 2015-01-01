package com.willeze.cubebots.tile;

import com.willeze.cubebots.CubeBots;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMarker extends BlockContainer {
	public Block renderBlock;
	public String renderEntity;
	public float size;

	public BlockMarker(String string, Block block, String e, float s) {
		super(Material.rock);
		setHardness(0.2F);
		renderBlock = block;
		renderEntity = e;
		size = s;
		setBlockBounds(0.5F - 0.2F * s, 0, 0.5F - 0.2F * s, 0.5F + 0.2F * s, 0.7F * s, 0.5F + 0.2F * s);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public TileEntity createInventoryTile(World world) {
		TileEntityMarker tile = (TileEntityMarker) createNewTileEntity(world, 0);
		tile.inv = true;
		return tile;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int b) {
		if (renderBlock != null) return new TileEntityMarker(renderBlock, null, size);
		if (renderEntity != null && renderEntity.length() > 0) {
			Entity e = EntityList.createEntityByName(renderEntity, var1);
			return new TileEntityMarker(null, e, size);
		}
		return new TileEntityMarker(renderBlock, null, size);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(CubeBots.MODID + ":" + this.getUnlocalizedName().substring(5));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return blockIcon;
	}
}
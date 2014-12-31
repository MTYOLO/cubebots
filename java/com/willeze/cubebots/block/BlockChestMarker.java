package com.willeze.cubebots.block;

import com.willeze.cubebots.CubeBots;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockChestMarker extends Block {
	
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;

	public BlockChestMarker() {
		super(Material.carpet);
		this.setCreativeTab(CubeBots.creativeTab);
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

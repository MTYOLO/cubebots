package com.willeze.cubebots.item;

import com.willeze.cubebots.CubeBots;
import com.willeze.cubebots.entity.CubeBot;
import com.willeze.cubebots.entity.CubeBotEnum;
import com.willeze.cubebots.entity.SpawnCubeBot;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCubeBotSpawner extends Item {

	CubeBotEnum type;

	public ItemCubeBotSpawner(CubeBotEnum type) {
		super();
		this.type = type;
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer p, World w, int x, int y, int z, int side, float par8, float par9, float par10) {
		if (w.isRemote)
			return super.onItemUse(is, p, w, x, y, z, side, par8, par9, par10);

		CubeBot cb;
		try {
			cb = SpawnCubeBot.doSpawn(w, type);
			cb.setPosition(x + 0.5, y + 1.3, z + 0.5);
			w.spawnEntityInWorld(cb);
			cb.getLookHelper().setLookPositionWithEntity(p, 30, 30);
			is.stackSize--;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

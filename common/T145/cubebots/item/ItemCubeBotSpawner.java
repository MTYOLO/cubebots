package T145.cubebots.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import T145.cubebots.entity.CreateCubeBot;
import T145.cubebots.entity.CubeBot;
import T145.cubebots.entity.EnumCubeBotType;

public class ItemCubeBotSpawner extends Item {
	EnumCubeBotType type;

	public ItemCubeBotSpawner(int i, EnumCubeBotType type) {
		super(i);
		this.type = type;
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer p, World w, int x, int y, int z, int side, float par8, float par9, float par10) {
		if (w.isRemote)
			return super.onItemUse(is, p, w, x, y, z, side, par8, par9, par10);

		CubeBot cb = CreateCubeBot.createCubeBot(w, type);
		cb.setPosition(x + 0.5, y + 1.3, z + 0.5);
		cb.setTamer(p);
		w.spawnEntityInWorld(cb);
		cb.getLookHelper().setLookPositionWithEntity(p, 30, 30);
		is.stackSize--;
		return true;
	}
}
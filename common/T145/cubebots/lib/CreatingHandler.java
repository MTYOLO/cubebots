package T145.cubebots.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IFuelHandler;

public class CreatingHandler implements ICraftingHandler, IFuelHandler {
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return 0;
	}
}
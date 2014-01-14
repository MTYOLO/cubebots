package T145.cubebots.objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockMarker extends BlockContainer {
	public Block renderBlock;
	public String renderEntity;
	public float size;

	public BlockMarker(int par1, Block block, String e, float s) {
		super(par1, Material.cloth);
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
		return 384;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public TileEntity createInventoryTile(World world) {
		TileMarker tile = (TileMarker) createNewTileEntity(world);
		tile.inv = true;
		return tile;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		if (renderBlock != null) return new TileMarker(renderBlock, null, size);
		if (renderEntity != null && renderEntity.length() > 0) {
			Entity e = EntityList.createEntityByName(renderEntity, var1);
			return new TileMarker(null, e, size);
		}
		return new TileMarker(renderBlock, null, size);
	}
}
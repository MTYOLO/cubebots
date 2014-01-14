package T145.cubebots.world;

import net.minecraft.entity.Entity;

public class BlockCoord {
	public int x, y, z;

	public BlockCoord(int a, int b, int c) {
		x = a;
		y = b;
		z = c;
	}

	public double getDistSqrTo(double x1, double y1, double z1) {
		return (x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z);
	}

	public double getDistSqrTo(Entity e) {
		return e.getDistanceSq(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof BlockCoord && ((BlockCoord) obj).x == x && ((BlockCoord) obj).y == y && ((BlockCoord) obj).z == z;
	}

	@Override
	public String toString() {
		return "(" + x + ";" + y + ";" + z + ")";
	}
}
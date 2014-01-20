package T145.cubebots.network.packet;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import T145.cubebots.entity.CreateCubeBot;
import T145.cubebots.entity.CubeBot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerServer implements ITickHandler {
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Random rnd = new Random();

		if (type.contains(TickType.WORLD)) {
			World world = (World) tickData[0];
			if (!world.playerEntities.isEmpty()) {
				EntityPlayer player = (EntityPlayer) world.playerEntities.get(new Random().nextInt(world.playerEntities.size()));

				Chunk chunk = world.getChunkFromBlockCoords((int) player.posX, (int) player.posZ);
				if (player.ticksExisted % 20 == 0 && rnd.nextInt(80) == 0) {
					for (int x = chunk.xPosition - 5; x <= chunk.xPosition + 5; x++) {
						for (int z = chunk.zPosition - 5; z <= chunk.zPosition + 5; z++) {
							if (Math.abs(x - chunk.xPosition) >= 3 + rnd.nextInt(2) && Math.abs(z - chunk.zPosition) >= 3 + rnd.nextInt(2)) {
								List list = world.getEntitiesWithinAABB(CubeBot.class, AxisAlignedBB.getBoundingBox(x * 16, 0, z * 16, x * 16 + 16, 40, z * 16 + 16));
								if (list.size() <= 2) CreateCubeBot.cubeBotSpawnInChunk(world, rnd, x, z);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "CubeBots TickHandler Server";
	}
}
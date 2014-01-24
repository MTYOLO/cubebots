package T145.cubebots.network.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandlerClient extends PacketHandler {
	public void readPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) throws Exception {
		ByteArrayInputStream inputArray = new ByteArrayInputStream(packet.data);
		DataInputStream in = new DataInputStream(inputArray);
		int type = in.readInt();
		if (type == 0) spawnParticleFromTo(in);
		in.close();
	}

	public void spawnParticleFromTo(DataInputStream in) throws Exception {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			World world = FMLClientHandler.instance().getClient().theWorld;
			String type = in.readUTF();
			double x1 = in.readDouble(), y1 = in.readDouble(), z1 = in.readDouble(), x2 = in.readDouble(), y2 = in.readDouble(), z2 = in.readDouble();
			Vec3 vec = Vec3.fakePool.getVecFromPool(x1 - x2, y1 - y2, z1 - z2);
			double length = vec.lengthVector();
			for (double b = 0.4; b < length; b += 0.8) {
				double px = x2 + vec.xCoord * b / length, py = y2 + vec.yCoord * b / length, pz = z2 + vec.zCoord * b / length;
				world.spawnParticle(type, px, py, pz, 0, 0, 0);
			}
		}
	}
}
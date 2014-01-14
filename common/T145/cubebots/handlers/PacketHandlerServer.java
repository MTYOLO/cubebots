package T145.cubebots.handlers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerServer extends PacketHandler {
	public void readPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) throws Exception {
		ByteArrayInputStream inputArray = new ByteArrayInputStream(packet.data);
		DataInputStream in = new DataInputStream(inputArray);
		int type = in.readInt();
		in.close();
	}
}

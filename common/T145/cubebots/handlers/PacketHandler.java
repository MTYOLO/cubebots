package T145.cubebots.handlers;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {
		try {
			readPacket(manager, packet, player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readPacket(INetworkManager manager, Packet250CustomPayload packet, Player player) throws Exception {}
}
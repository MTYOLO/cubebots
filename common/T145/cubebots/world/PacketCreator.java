package T145.cubebots.world;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketCreator {
	Packet250CustomPayload pk;
	ByteArrayOutputStream outArray;
	DataOutputStream out;

	public PacketCreator(String s) {
		pk = new Packet250CustomPayload();
		pk.channel = s;
		outArray = new ByteArrayOutputStream();
		out = new DataOutputStream(outArray);
	}

	public Packet250CustomPayload getPacket() {
		return pk;
	}

	public void finishWriting() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pk.data = outArray.toByteArray();
	}

	public void writeBoolean(boolean i) {
		try {
			out.writeBoolean(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeInt(int i) {
		try {
			out.writeInt(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeString(String s) {
		try {
			out.writeUTF(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeByte(byte i) {
		try {
			out.writeByte(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFloat(float i) {
		try {
			out.writeFloat(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeDouble(double i) {
		try {
			out.writeDouble(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
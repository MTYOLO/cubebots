package T145.cubebots.objects;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import T145.cubebots.entities.CubeBot;
import T145.cubebots.world.PacketCreator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ItemCubeCall extends Item
{
	boolean instant;
	
	public ItemCubeCall(int i, boolean b)
	{
		super(i);
		instant = b;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer e)
	{
		if(!instant)
		{
			List<CubeBot> list = w.getEntitiesWithinAABB(CubeBot.class, e.boundingBox.expand(32, 32, 32));
			if(!list.isEmpty())
			{
				for(int a=0 ; a<list.size() ; a++)
				{
					CubeBot cb = list.get(a);
					if(cb.getIsTamed() && cb.tamer==e.username)
					{
						cb.setMoveTo(e, 0.29F);

						PacketCreator creator = new PacketCreator("CubeBots");
						creator.writeInt(0);
						creator.writeString("flame");
						creator.writeDouble(cb.posX);
						creator.writeDouble((cb.boundingBox.maxY+cb.boundingBox.minY)/2);
						creator.writeDouble(cb.posZ);
						creator.writeDouble(e.posX);
						creator.writeDouble((e.boundingBox.maxY+e.boundingBox.minY)/2);
						creator.writeDouble(e.posZ);
						creator.finishWriting();
						PacketDispatcher.sendPacketToPlayer(creator.getPacket(), (Player) e);
					}
				}
			}
		}
		else
		{
			for(int a=0 ; a<w.getLoadedEntityList().size() ; a++)
			{
				Entity ent = (Entity) w.getLoadedEntityList().get(a);
				if(ent instanceof CubeBot)
				{
					CubeBot cb = (CubeBot)ent;
					if(cb.getIsTamed() && cb.tamer==e.username)
					{
						Vec3 vec = Vec3.fakePool.getVecFromPool(cb.posX-e.posX, cb.posY-e.posY, cb.posZ-e.posZ);
						double rate = vec.lengthVector()>2.5 ? 2.5/vec.lengthVector() : 1;
						cb.setPosition(e.posX+vec.xCoord*rate, e.posY+vec.yCoord*rate, e.posZ+vec.zCoord*rate);
						cb.getNavigator().clearPathEntity();
						cb.setVelocity(0, 0, 0);
						cb.fallDistance = 0;
						
						PacketCreator creator = new PacketCreator("CubeBots");
						creator.writeInt(0);
						creator.writeString("portal");
						creator.writeDouble(cb.posX);
						creator.writeDouble((cb.boundingBox.maxY+cb.boundingBox.minY)/2);
						creator.writeDouble(cb.posZ);
						creator.writeDouble(e.posX);
						creator.writeDouble((e.boundingBox.maxY+e.boundingBox.minY)/2);
						creator.writeDouble(e.posZ);
						creator.finishWriting();
						PacketDispatcher.sendPacketToPlayer(creator.getPacket(), (Player) e);
					}
				}
			}
		}
		return super.onItemRightClick(is, w, e);
	}
}

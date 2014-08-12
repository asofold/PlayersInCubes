package me.asofold.bpl.pic.net.cb3026;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendPacketsCB3026 implements SendPackets {

	public SendPacketsCB3026(){
		((org.bukkit.craftbukkit.v1_7_R2.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB3026 (MC 1.7.5)
			((org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new net.minecraft.server.v1_7_R2.PacketPlayOutPlayerInfo(playerListName, online, ping)
					);
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

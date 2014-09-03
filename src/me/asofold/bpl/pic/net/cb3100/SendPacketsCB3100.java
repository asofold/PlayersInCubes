package me.asofold.bpl.pic.net.cb3100;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendPacketsCB3100 implements SendPackets {

	public SendPacketsCB3100(){
		((org.bukkit.craftbukkit.v1_7_R4.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB3026 (MC 1.7.10)
			((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo(playerListName, online, ping)
					);
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

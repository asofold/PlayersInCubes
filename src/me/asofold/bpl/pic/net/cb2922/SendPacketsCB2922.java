package me.asofold.bpl.pic.net.cb2922;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendPacketsCB2922 implements SendPackets {

	public SendPacketsCB2922(){
		((org.bukkit.craftbukkit.v1_7_R1.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB2922 (MC 1.7.2)
			((org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new net.minecraft.server.v1_7_R1.PacketPlayOutPlayerInfo(playerListName, online, ping)
					);
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

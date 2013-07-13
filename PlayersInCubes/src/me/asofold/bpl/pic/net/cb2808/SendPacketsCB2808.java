package me.asofold.bpl.pic.net.cb2808;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

public class SendPacketsCB2808 implements SendPackets {

	public SendPacketsCB2808(){
		((org.bukkit.craftbukkit.v1_6_R2.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB2808 (MC 1.6.2)
			((org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new 
					net.minecraft.server.v1_6_R2.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

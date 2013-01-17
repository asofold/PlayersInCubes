package me.asofold.bpl.pic.net.cb2511;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendPacketsCB2511 implements SendPackets{
	public SendPacketsCB2511(){
		((org.bukkit.craftbukkit.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(Player player, String playerListName, boolean online, int ping)
	{
		try{
			// CB2511
			((org.bukkit.craftbukkit.entity.CraftPlayer) player).getHandle().netServerHandler.sendPacket(
					new net.minecraft.server.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

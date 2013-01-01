package me.asofold.bpl.pic.net.cb2511;

import org.bukkit.entity.Player;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

public class SendPacketsCB2511 implements SendPackets{

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

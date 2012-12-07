package me.asofold.bpl.pic.net.cb2512;

import org.bukkit.entity.Player;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

public class SendPacketsCB2512 implements SendPackets {

	@Override
	public boolean sendAllPacket201(Player player, String playerListName, boolean online, int ping)
	{
		try{
			// CB2512 +
			((org.bukkit.craftbukkit.v1_4_5.entity.CraftPlayer) player).getHandle().netServerHandler.sendPacket(
					new net.minecraft.server.v1_4_5.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t2){};
		return false;
	}

}

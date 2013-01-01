package me.asofold.bpl.pic.net.cb2545;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

import org.bukkit.entity.Player;

public class SendPacketsCB2545 implements SendPackets{

	@Override
	public boolean sendAllPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB2545
			((org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new 
					net.minecraft.server.v1_4_6.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

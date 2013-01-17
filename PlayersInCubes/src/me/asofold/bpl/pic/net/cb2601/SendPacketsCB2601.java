package me.asofold.bpl.pic.net.cb2601;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendPacketsCB2601 implements SendPackets{
	
	public SendPacketsCB2601(){
		((org.bukkit.craftbukkit.v1_4_R1.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB2601
			((org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new 
					net.minecraft.server.v1_4_R1.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

package me.asofold.bpl.pic.net.cb2763;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

public class SendPacketsCB2763 implements SendPackets {
	
	public SendPacketsCB2763(){
		((org.bukkit.craftbukkit.v1_5_R3.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB2763 (MC 1.5.2)
			((org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new 
					net.minecraft.server.v1_5_R3.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

package me.asofold.bpl.pic.net.cb2645;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;

public class SendPacketsCB2645 implements SendPackets {
	
	public SendPacketsCB2645(){
		((org.bukkit.craftbukkit.v1_5_R1.CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping)
	{
		try{
			// CB2645
			((org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer) player).getHandle().playerConnection.sendPacket(
					new 
					net.minecraft.server.v1_5_R1.Packet201PlayerInfo(playerListName, online, ping));
			return true;
		}
		catch(Throwable t1){}
		return false;
	}

}

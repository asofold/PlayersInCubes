package me.asofold.bpl.pic.net;

import org.bukkit.entity.Player;

public class SendPacketsFactory {
	public static interface SendPackets{
		public boolean sendAllPacket201(final Player player, final String playerListName, final boolean online, final int ping);
	}
	
	private static SendPackets dummySendPackets = new SendPackets() {
		@Override
		public boolean sendAllPacket201(Player player, String playerListName, boolean online, int ping)
		{
			return false;
		}
	};

	public SendPackets getSendPackets() {
		try{
			return new me.asofold.bpl.pic.net.cb2511.SendPacketsCB2511();
		} catch(Throwable t){};
		
		try{
			return new me.asofold.bpl.pic.net.cb2512.SendPacketsCB2512();
		} catch(Throwable t){};
		
		try{
			return new me.asofold.bpl.pic.net.cb2545.SendPacketsCB2545();
		} catch(Throwable t){};
		
		return dummySendPackets;
	}

}

package me.asofold.bpl.pic.net;

import org.bukkit.entity.Player;

public class SendPacketsFactory {
	public static interface SendPackets{
		public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping);
	}
	
	private static SendPackets dummySendPackets = new SendPackets() {
		@Override
		public boolean sendPacket201(Player player, String playerListName, boolean online, int ping)
		{
			return false;
		}
	};

	public SendPackets getSendPackets() {
		try{
			return new me.asofold.bpl.pic.net.cb3026.SendPacketsCB3026();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2922.SendPacketsCB2922();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2882.SendPacketsCB2882();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2808.SendPacketsCB2808();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2794.SendPacketsCB2794();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2763.SendPacketsCB2763();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2691.SendPacketsCB2691();
		} catch(Throwable t){};
		try{
			return new me.asofold.bpl.pic.net.cb2645.SendPacketsCB2645();
		} catch(Throwable t){};
		return dummySendPackets;
	}

}

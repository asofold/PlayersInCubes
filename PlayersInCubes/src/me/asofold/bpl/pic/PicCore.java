package me.asofold.bpl.pic;

import java.io.File;
import java.util.Collection;

import me.asofold.bpl.pic.config.PicSettings;
import me.asofold.bpl.pic.cubelib.AbstractCubeCore;
import me.asofold.bpl.pic.cubelib.server.CubePlayer;
import me.asofold.bpl.pic.stats.Stats;
import net.minecraft.server.Packet201PlayerInfo;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Core functionality.
 * @author mc_dev
 *
 */
public final class PicCore extends AbstractCubeCore<PicSettings>{

	private static final Stats stats = new Stats("[PIC]");
	
	private File dataFolder = null;
	
	public PicCore() {
		super(new PicSettings());
	}
	
	public final void setDataFolder(final File dataFolder) {
		this.dataFolder = dataFolder;
	}
	
	public final boolean reload() {
		final File file = new File(dataFolder, "config.yml");
		final PicSettings settings = PicSettings.load(file);
		if (settings != null){
			applySettings(settings);
			// Consider logging that settings were reloaded.
			return true;
		}
		else return false;
	}
	
	/**
	 * This will alter and save the settings, unless no change is done.
	 * @param enabled
	 * @return If this was a state change.
	 */
	public final boolean setEnabled(final boolean enabled) {
		File file = new File(dataFolder, "config.yml");
		if (!(enabled ^ this.enabled)){
			if (!file.exists()) settings.save(file);
			return false;
		}
		this.enabled = enabled;
		if (enabled) checkAllOnlinePlayers();
		else clear(false); // Renders all visible.
		settings.enabled = enabled;
		settings.save(file);
		return true;
	}
	
	public final void hidePlayer(final Player player, final Player playerToHide){
		player.hidePlayer(playerToHide);
		if (settings.sendPackets) ((CraftPlayer) player).getHandle().netServerHandler.sendPacket(new Packet201PlayerInfo(playerToHide.getPlayerListName(), true, 9999));
	}
	
	@Override
	public final void checkOut(final Player player) {
		if (!enabled) return;
		final String playerName = player.getName();
		final CubePlayer pp = players.get(playerName);
		if (pp == null){ // contract ?
			for (final CubePlayer opp : players.values()){
				if (opp.playerName.equals(playerName)) continue;
				if (opp.bPlayer.canSee(player)) hidePlayer(opp.bPlayer, player); // opp.bPlayer.hidePlayer(player);
				if (player.canSee(opp.bPlayer)) hidePlayer(player, opp.bPlayer); //  player.hidePlayer(opp.bPlayer);
			}
		}
		else{
			super.checkOut(pp);
		}
	}
	
	@Override
	public final void onQuit(final Player player) {
		super.onQuit(player);
		if (settings.sendPackets){
			final String listName = player.getPlayerListName();
			// TODO: this remains fishy, somewhat.
			((CraftServer) Bukkit.getServer()).getHandle().sendAll(new Packet201PlayerInfo(listName, false, 9999));
		}
	}
	
	/**
	 * If outOfRange is set to false, this will show all players to all other players (quadratic time).
	 */
	@Override
	protected final void removeAllPlayers(final boolean outOfRange) {
		if (!outOfRange){
			// Costly: basically quadratic time all vs. all.
			final Player[] online = Bukkit.getOnlinePlayers();
			for (final CubePlayer pp : players.values()){
				for (final Player other : online){
					if (!other.canSee(pp.bPlayer)) other.showPlayer(pp.bPlayer);
					if (!pp.bPlayer.canSee(other)) pp.bPlayer.showPlayer(other);
				}
			}
		}
		super.removeAllPlayers(outOfRange);
	}
	
	@Override
	public final void outOfRange(final CubePlayer pp, final Collection<String> names) {
		final Player player = pp.bPlayer;
		for (final String name : names){
			final CubePlayer opp = players.get(name);
			 if (opp == null) continue; // TODO: ERROR, find out.
			if (player.canSee(opp.bPlayer)) hidePlayer(player, opp.bPlayer); //player.hidePlayer(opp.bPlayer);
			if (opp.bPlayer.canSee(player)) hidePlayer(opp.bPlayer, player); //opp.bPlayer.hidePlayer(player);
		}
	}
	
	@Override
	public final void inRange(final CubePlayer pp, final Collection<String> names) {
		final Player player = pp.bPlayer;
		for (final String name : names){
			final CubePlayer opp = players.get(name);
			 if (opp == null) continue; // TODO: ERROR, find out.
			if (!player.canSee(opp.bPlayer)) player.showPlayer(opp.bPlayer);
			if (!opp.bPlayer.canSee(player)) opp.bPlayer.showPlayer(player);
		}
	}

	@Override
	public final Stats getStats(){
		return stats;
	}
	
}

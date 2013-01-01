package me.asofold.bpl.pic;

import java.io.File;
import java.util.Collection;

import me.asofold.bpl.pic.config.PicSettings;
import me.asofold.bpl.pic.cubelib.AbstractCubeCore;
import me.asofold.bpl.pic.cubelib.server.CubePlayer;
import me.asofold.bpl.pic.net.SendPacketsFactory;
import me.asofold.bpl.pic.net.SendPacketsFactory.SendPackets;
import me.asofold.bpl.pic.stats.Stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Core functionality.
 * @author mc_dev
 *
 */
public final class PicCore extends AbstractCubeCore<PicSettings>{

	private static final Stats stats = new Stats("[PIC]");
	
	private File dataFolder = null;
	
	private final SendPackets sendPackets;
	
//	private final EntityTracker entityTracker = new EntityTracker(this);
	
	public PicCore() {
		super(new PicSettings());
		sendPackets = new SendPacketsFactory().getSendPackets();
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
		if (settings.sendPackets){
			sendPacket201(player, playerToHide.getName(), true);
		}
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
			final String playerName = player.getName();
			for (final Player other : Bukkit.getServer().getOnlinePlayers()){
				// Heuristic, somewhat.
				if (!other.canSee(player)) sendPacket201(other, playerName, false);
			}
		}
	}
	
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online){
		return sendPacket201(player, playerListName, online, 9999);
	}
	
	public boolean sendPacket201(final Player player, final String playerListName, final boolean online, final int ping) {
		// TODO: this remains fishy, somewhat.
		return sendPackets.sendPacket201(player, playerListName, online, ping);
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

	@Override
	public void applySettings(PicSettings settings) {
		super.applySettings(settings);
//		entityTracker.applySettings(settings);
	}
	
	
	
	@Override
	public void clear(boolean blind) {
		super.clear(blind);
//		entityTracker.clear();
	}

	/**
	 * To be called in onEnable.
	 * @param canTrackEntities
	 * @return
	 */
	public Listener[] getExtraListeners(boolean canTrackEntities){
		
//		if (canTrackEntities) return new Listener[]{entityTracker.getListener()};
		
		return new Listener[0];
		
	}
	
//	public EntityTracker getEntityTracker(){
//		return entityTracker;
//	}
	
}

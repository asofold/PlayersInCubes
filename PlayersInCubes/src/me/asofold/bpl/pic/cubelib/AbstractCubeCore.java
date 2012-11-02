package me.asofold.bpl.pic.cubelib;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import me.asofold.bpl.pic.cubelib.config.CubeSettings;
import me.asofold.bpl.pic.cubelib.server.CubePlayer;
import me.asofold.bpl.pic.cubelib.server.CubeServer;
import me.asofold.bpl.pic.cubelib.server.ICubeCore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class AbstractCubeCore<S extends CubeSettings> implements ICubeCore{
	
	
	protected boolean enabled = true;
	
	/**
	 * CubeSettings.
	 */
	protected S settings;
	
	/**
	 * World specific CubeServer. Every world must have one to keep track of players inside of worlds.
	 */
	protected final Map<String, CubeServer> cubeServers = new LinkedHashMap<String, CubeServer>(53);
	 
	/**
	 * Player specific data / lookup.
	 */
	protected final Map<String, CubePlayer> players = new LinkedHashMap<String, CubePlayer>(517);
	
	public AbstractCubeCore(S settings){
		this.settings = settings;
	}
	
	public final void applySettings(final S settings) {
		// Later: maybe try for a lazy transition or a hard one depending on changes.
		this.settings = settings;
		cleanup();
		enabled = settings.enabled;
	}
	
	public S getSettings(){
		return settings;
	}
	
	public final boolean isEnabled(){
		return enabled;
	}
	
	/**
	 * Remove all players, remove all data, check in all players again.
	 */
	public final void cleanup() {
		clear(true);
		checkAllOnlinePlayers();
	}
	
	public final void clear(final boolean blind){
		removeAllPlayers(blind);
		for (final CubeServer server : cubeServers.values()){
			server.clear();
		}
		cubeServers.clear();
	}
	
	public final void checkAllOnlinePlayers() {
		if (!enabled) return;
		for (final Player player : Bukkit.getOnlinePlayers()){
			check(player, player.getLocation());
		}
	}
	
	/**
	 * Does currently not remove players from the CubeServer players sets. 
	 * @param outOfRange Render players out of range or just check out all.
	 */
	protected void removeAllPlayers(final boolean outOfRange) {
		if (outOfRange){
			for (final CubePlayer pp : players.values()){
				outOfRange(pp, pp.checkOut());
			}
		}
		else{
			for (final CubePlayer pp : players.values()){
				pp.checkOut(); // Ignore return value.
			}
		}
		players.clear();
	}
	
	/**
	 * Designed for listener calls (quit, kick).
	 * @param player
	 */
	public void onQuit(final Player player) {
		checkOut(player);
	}
	
	/**
	 * Meant to override for general purpose (quit,kick).
	 * @param player
	 */
	public void checkOut(final Player player) {
		if (!enabled) return;
		final String playerName = player.getName();
		final CubePlayer pp = players.get(playerName);
		if (pp != null) checkOut(pp);
	}
	
	/**
	 * Called for present player data (quit, kick).
	 * @param player
	 */
	public final void checkOut(final CubePlayer pp) {
		if (!enabled) return;
		if (pp.world != null) getCubeServer(pp.world).players.remove(pp.playerName);
		outOfRange(pp, pp.checkOut());
		players.remove(pp.playerName); // TODO: maybe hold data longer.
	}

	/**
	 * Join or respawn.
	 * @param player
	 * @param location 
	 */
	public final void checkIn(final Player player, Location location) {
		checkOut(player);
		check(player, player.getLocation());
	}
	
	/**
	 * Get CubePlayer, put to internals, if not present.
	 * @param player
	 * @return
	 */
	private final CubePlayer getCubePlayer(final Player player){
		final String name = player.getName();
		final CubePlayer pp = players.get(name);
		if (pp != null) return pp;
		final CubePlayer npp = new CubePlayer(player, getStats());
		players.put(name, npp);
		return npp;
	}
	
	/**
	 * Get the cube server for the world, will create it if not yet existent.
	 * @param world Exact case.
	 * @return
	 */
	protected final CubeServer getCubeServer(final String world) {
		CubeServer server = cubeServers.get(world);
		if (server == null){
			server = new CubeServer(world, this, settings.cubeSize);
			cubeServers.put(world, server);
		}
		return server;
	}
	
	/**
	 * Lighter check: Use this for set up players.
	 * @param player
	 * @param to NOT NULL
	 */
	public final void check(final Player player, final Location to) {
		if (!enabled) return;
		final CubePlayer pp =  getCubePlayer(player);
		final String world = to.getWorld().getName();
		if (settings.ignoreWorlds.contains(world)){
			// Moving in a ignored world.
			if (pp.world == null){
				// New data.
			}
			else if (world.equals(pp.world)){
				// Already inside, no changes.
				return;
			}
			else{
				// World change, remove from old CubeServer.
				getCubeServer(pp.world).remove(pp);
			}
			// World change or new.
			getCubeServer(world).add(pp, false);
			pp.world = world;
			pp.tsLoc = 0; // necessary.
			// else: keep ignoring.
			return;
		}
		final int x = to.getBlockX();
		final int y = to.getBlockY();
		final int z = to.getBlockZ();
		final long ts = System.currentTimeMillis();
		
		// Check if to set the position:
		if (!world.equals(pp.world)){
			// World change into a checked world.
			// Add to new CubeServer:
			getCubeServer(world).add(pp, true);
			// Check removal:
			if (pp.world == null){
				// Was a new player.
			}
			else{
				// Remove from old server (light).
				getCubeServer(pp.world).remove(pp);
			}
		}
		else if (settings.durExpireData > 0  && ts - pp.tsLoc > settings.durExpireData){
			// Expired, set new (no world change).
		}
		else if (pp.inRange(x, y, z, settings.distLazy)){
			// Still in range, quick return (no world change).
			return;
		}
		else{
			// Out of range set new (no world change).
		}
		
		// Set position.
		pp.tsLoc = ts;
		pp.world = world;
		pp.x = x;
		pp.y = y;
		pp.z = z;
		
		// Add player to all cubes !
		getCubeServer(world).update(pp, settings.distCube);
	}
	
	public abstract void outOfRange(final CubePlayer pp, final Collection<String> names);
	
	public abstract void inRange(final CubePlayer pp, final Collection<String> names);
	
	
}

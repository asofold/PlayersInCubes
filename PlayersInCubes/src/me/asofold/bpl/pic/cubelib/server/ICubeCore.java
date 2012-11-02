package me.asofold.bpl.pic.cubelib.server;

import java.util.Collection;

import me.asofold.bpl.pic.stats.Stats;

/**
 * Interface for the core functionality to be called from cube servers.
 * @author mc_dev
 *
 */
public interface ICubeCore {

	public void outOfRange(CubePlayer pp, Collection<String> names);

	public void inRange(CubePlayer pp, Collection<String> names);
	
	public Stats getStats();

}

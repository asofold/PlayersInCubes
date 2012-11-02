package me.asofold.bpl.pic.core;

import java.util.Collection;

import me.asofold.bpl.pic.stats.Stats;

/**
 * Interface for the core functionality to be called from cube servers.
 * @author mc_dev
 *
 */
public interface ICore {

	public void outOfRange(PicPlayer pp, Collection<String> names);

	public void inRange(PicPlayer pp, Collection<String> names);
	
	public Stats getStats();

}

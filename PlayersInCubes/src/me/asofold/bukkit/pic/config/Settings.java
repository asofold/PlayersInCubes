package me.asofold.bukkit.pic.config;

import java.io.File;


/**
 * Configuration settings.
 * @author mc_dev
 *
 */
public class Settings {
	
	/**
	 * Size of a cube.
	 */
	public int cubeSize = 25;
	
	/**
	 * Distance at which players get associated with cubes.
	 */
	public int distCube = 25;
	
	/**
	 * Distance a player must take to get re checked.
	 */
	public int distLazy = 5;
	
	/**
	 * Duration after which a player gets checked again.
	 */
	public long durExpireData = 10000;
	
	

	public static Settings load(File dataFolder) {
		// TODO 
		return new Settings();
	}
	
}

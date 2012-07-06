package me.asofold.bukkit.pic.config;

import java.io.File;

import me.asofold.bukkit.pic.config.compatlayer.CompatConfig;
import me.asofold.bukkit.pic.config.compatlayer.CompatConfigFactory;


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
	public int distCube = 16;
	
	/**
	 * Distance a player must take to get re checked.
	 */
	public int distLazy = 5;
	
	/**
	 * Duration after which a player gets checked again.
	 */
	public long durExpireData = 10000;
	
	public boolean save(File dataFolder){
		// TODO
		return false;
	}

	public static Settings load(File file) {
//		CompatConfig cfg = CompatConfigFactory.getConfig(file);
		return new Settings();
	}
	
}

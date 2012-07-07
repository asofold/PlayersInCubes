package me.asofold.bukkit.pic.config;

import java.io.File;

import me.asofold.bukkit.pic.config.compatlayer.CompatConfig;
import me.asofold.bukkit.pic.config.compatlayer.CompatConfigFactory;
import me.asofold.bukkit.pic.config.compatlayer.ConfigUtil;


/**
 * Configuration settings.
 * @author mc_dev
 *
 */
public class Settings {
	
	static final String pathEnabled = "enabled";
	static final String pathCubeSize = "cube.size";
	static final String pathDistCube = "cube.distance";
	static final String pathDistLazy = "lazy.distance";
	static final String pathDurExpireData = "lazy.lifetime";
	
	/**
	 * If to do checks at all.
	 */
	public boolean enabled = true;
	
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
	
	public boolean save(File file){
		CompatConfig cfg  = CompatConfigFactory.getConfig(file);
		toConfig(cfg);
		return cfg.save();
	}
	
	public static CompatConfig getDefaultConfig(){
		CompatConfig cfg = CompatConfigFactory.getConfig(null);
		Settings ref = new Settings();
		ref.toConfig(cfg);
		return cfg;
	}

	private void toConfig(CompatConfig cfg) {
		cfg.set(pathEnabled, true);
		cfg.set(pathCubeSize, cubeSize);
		cfg.set(pathDistCube, distCube);
		cfg.set(pathDistLazy, distLazy);
		cfg.set(pathDurExpireData, durExpireData / 1000); // Saved in seconds
	}

	/**
	 * Does update defaults and save back if changed. 
	 * @param file
	 * @return
	 */
	public static Settings load(File file) {
		CompatConfig cfg = CompatConfigFactory.getConfig(file);
		cfg.load();
		CompatConfig defaults = getDefaultConfig();
		if (ConfigUtil.forceDefaults(defaults, cfg)) cfg.save();
		Settings settings = new Settings();
		Settings ref = new Settings();
		settings.enabled = cfg.getBoolean(pathEnabled, ref.enabled);
		settings.cubeSize = cfg.getInt(pathCubeSize, ref.cubeSize);
		settings.distCube = cfg.getInt(pathDistCube, ref.distCube);
		settings.distLazy = cfg.getInt(pathDistLazy, ref.distLazy);
		settings.durExpireData = cfg.getLong(pathDurExpireData, ref.durExpireData / 1000) * 1000; // Saved in seconds
		return settings;
	}
	
}

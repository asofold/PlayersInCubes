package me.asofold.bpl.pic.cubelib.config;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Bukkit;

import me.asofold.bpl.pic.config.compatlayer.CompatConfig;
import me.asofold.bpl.pic.config.compatlayer.CompatConfigFactory;
import me.asofold.bpl.pic.config.compatlayer.ConfigUtil;


/**
 * Configuration settings.
 * @author mc_dev
 *
 */
public class CubeSettings {
	
	protected static final String pathEnabled = "enabled";
	protected static final String pathCubeSize = "cube.size";
	protected static final String pathDistCube = "cube.distance";
	protected static final String pathDistLazy = "lazy.distance";
	protected static final String pathDurExpireData = "lazy.lifetime";
	protected static final String pathPackets = "packets";
//	static final String pathBroadcastQuit = pathPackets + ".broadcast-quit";
	protected static final String pathIgnoreWorlds = "ignore-worlds";
	
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
	public long durExpireData = 0;
	
	/**
	 * Worlds to ignore, exact case.
	 */
	public final Set<String> ignoreWorlds = new HashSet<String>();
	
	
	public boolean save(File file){
		checkFile(file);
		CompatConfig cfg  = CompatConfigFactory.getConfig(file);
		toConfig(cfg);
		return cfg.save();
	}

	public void toConfig(CompatConfig cfg) {
		cfg.set(pathEnabled, enabled);
		cfg.set(pathCubeSize, cubeSize);
		cfg.set(pathDistCube, distCube);
		cfg.set(pathDistLazy, distLazy);
		cfg.set(pathDurExpireData, durExpireData / 1000); // Saved in seconds
		cfg.set(pathIgnoreWorlds, new LinkedList<String>(ignoreWorlds));
	}

	/**
	 * Does update defaults and save back if changed. 
	 * @param file
	 * @return
	 */
	public static CubeSettings loadSettings(CubeSettings settings, CompatConfig cfg) {
		CubeSettings ref = new CubeSettings();
		settings.enabled = cfg.getBoolean(pathEnabled, ref.enabled);
		settings.cubeSize = cfg.getInt(pathCubeSize, ref.cubeSize);
		settings.distCube = cfg.getInt(pathDistCube, ref.distCube);
		settings.distLazy = cfg.getInt(pathDistLazy, ref.distLazy);
		settings.durExpireData = cfg.getLong(pathDurExpireData, ref.durExpireData / 1000) * 1000; // Saved in seconds
        ConfigUtil.readStringSetFromList(cfg, pathIgnoreWorlds, settings.ignoreWorlds, true, true, false);
		return settings;
	}

	public static void checkFile(File file) {
		if (!file.exists()){
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				Bukkit.getLogger().severe("[CubeSettings] Could not create empty file: " + file.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}
	
}

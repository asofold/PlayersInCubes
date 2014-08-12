package me.asofold.bpl.pic.config;

import java.io.File;

import me.asofold.bpl.pic.config.compatlayer.CompatConfig;
import me.asofold.bpl.pic.config.compatlayer.CompatConfigFactory;
import me.asofold.bpl.pic.config.compatlayer.ConfigUtil;
import me.asofold.bpl.pic.cubelib.config.CubeSettings;

public class PicSettings extends CubeSettings {
	protected static final String pathSendPackets = pathPackets + ".enabled";
	/**
	 * To send fake packets to keep / remove players in/from the tab player list.
	 */
	public boolean sendPackets = true;
	
	public static CompatConfig getDefaultConfig(){
		CompatConfig cfg = CompatConfigFactory.getConfig(null);
		PicSettings ref = new PicSettings();
		ref.toConfig(cfg);
		return cfg;
	}

	public static PicSettings load(File file) {
		CompatConfig cfg = CompatConfigFactory.getConfig(file);
		cfg.load();
		CompatConfig defaults = getDefaultConfig();
		if (ConfigUtil.forceDefaults(defaults, cfg)){
			checkFile(file);
			cfg.save();
		}
		PicSettings settings = new PicSettings();
		CubeSettings.loadSettings(settings, cfg);
		settings.sendPackets = cfg.getBoolean(pathSendPackets, settings.sendPackets);
		return settings;
	}
	
	public void toConfig(CompatConfig cfg) {
		super.toConfig(cfg);
		cfg.set(pathSendPackets, sendPackets);
	}

}

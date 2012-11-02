package me.asofold.bpl.pic.config;

import java.io.File;

import me.asofold.bpl.pic.config.compatlayer.CompatConfig;
import me.asofold.bpl.pic.config.compatlayer.CompatConfigFactory;
import me.asofold.bpl.pic.config.compatlayer.ConfigUtil;

public class PicSettings extends Settings {
	
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
		Settings.loadSettings(settings, cfg);
		settings.sendPackets = cfg.getBoolean(pathSendPackets, settings.sendPackets);
		return settings;
	}
	
	public void toConfig(CompatConfig cfg) {
		super.toConfig(cfg);
		cfg.set(pathSendPackets, sendPackets);
	}

}

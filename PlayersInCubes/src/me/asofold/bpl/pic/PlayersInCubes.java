package me.asofold.bpl.pic;

import me.asofold.bpl.pic.cubelib.config.CubeSettings;
import me.asofold.bpl.pic.cubelib.listener.CubeListener;
import me.asofold.bpl.pic.util.Utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayersInCubes extends JavaPlugin {
	
	private final PicCore core = new PicCore();
	private final CubeListener listener = new CubeListener(core);
	
	public PlayersInCubes(){
		core.setDataFolder(getDataFolder());
	}

	@Override
	public final void onDisable() {
		core.clear(false); // Render seen, if just this is disabled.
		System.out.println("[PIC] " + getDescription().getFullName() +" has been disabled.");
	}

	@Override
	public final void onEnable() {
		core.setDataFolder(getDataFolder());
		core.reload();
		getServer().getPluginManager().registerEvents(listener, this);
		System.out.println("[PIC] " + getDescription().getFullName() +" is now enabled.");
	}
	
	@Override
	public final boolean onCommand(final CommandSender sender, final Command command,
			String label, final String[] args) {
		if (command != null) label = command.getLabel();
		label = label.trim().toLowerCase();
		if (!label.equals("playersincubes")) return false;
		final int len = args.length;
		String cmd = null;
		if (len > 0) cmd = args[0].trim().toLowerCase();
		if (len == 1 && cmd.equals("reload")){
			if (!Utils.checkPerm(sender, "playersincubes.reload")) return true;
			if (core.reload()) sender.sendMessage("[PIC] CubeSettings reloaded.");
			else sender.sendMessage("[PIC] Reloading the settings failed.");
			return true;
		}
		else if (len == 1 && cmd.equals("stats")){
			if (!Utils.checkPerm(sender, "playersincubes.stats.view")) return true;
			sender.sendMessage(core.getStats().getStatsStr(sender instanceof Player));
			return true;
		}
		else if (len == 2 && cmd.equals("stats") && args[1].equalsIgnoreCase("reset")){
			if (!Utils.checkPerm(sender, "playersincubes.stats.reset")) return true;
			core.getStats().clear();
			sender.sendMessage("[PIC] Stats reset.");
			return true;
		}
		else if (len == 1 && cmd.equals("cleanup")){
			if (!Utils.checkPerm(sender, "playersincubes.stats.cleanup")) return true;
			core.cleanup();
			sender.sendMessage("[PIC] Cleanup done: re-checked all players.");
			return true;
		}
		else if (len == 1 && cmd.equals("info")){
			if (!Utils.checkPerm(sender, "playersincubes.info")) return true;
			sender.sendMessage(getInfoMessage());
			return true;
		}
		else if (len == 1 && cmd.equals("disable")){
			if (!Utils.checkPerm(sender, "playersincubes.disable")) return true;
			if (core.setEnabled(false))	sender.sendMessage("[PIC] Rendered all visible and disabled checking.");
			else sender.sendMessage("[PIC] PlayersInCubes was already disabled.");
			return true;
		}
		else if (len == 1 && cmd.equals("enable")){
			if (!Utils.checkPerm(sender, "playersincubes.enable")) return true;
			if (core.setEnabled(true))	sender.sendMessage("[PIC] PlayersInCubes is now enabled.");
			else sender.sendMessage("[PIC] PlayersInCubes was already enabled.");
			return true;
		}
		return false;
	}
	
	public final String getInfoMessage() {
		final CubeSettings settings = core.getSettings();
		final StringBuilder b = new StringBuilder();
		b.append("[PIC][INFO] PlayersInCubes " + getDescription().getVersion() +" is ");
		b.append((settings.enabled ? "enabled.":"DISABLED."));
		b.append(" cube.size=" + settings.cubeSize);
		b.append(" cube.distance=" + settings.distCube);
		b.append(" lazy.distance=" + settings.distLazy);
		b.append(" lazy.lifetime=" + (settings.durExpireData / 1000));
//		b.append(" | ");
		b.append(" | (More: /pic stats)");
		return b.toString();
	}
	
}

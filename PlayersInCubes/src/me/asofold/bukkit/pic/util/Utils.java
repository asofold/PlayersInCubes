package me.asofold.bukkit.pic.util;

import org.bukkit.command.CommandSender;

public final class Utils {
	
	/**
	 * Block coordinate for double, especially important for negative numbers.
	 * (Adapted From Bukkit/NumberConversions.)
	 * @param x
	 * @return
	 */
	public static final int floor(final double x) {
        final int floor = (int) x;
        return (floor == x)? floor : floor - (int) (Double.doubleToRawLongBits(x) >>> 63);
    }
	
	public static boolean hasPermission(CommandSender sender, String perm){
		return sender.isOp() || sender.hasPermission(perm);
	}
	
	public static boolean checkPerm(CommandSender sender, String perm){
		if (hasPermission(sender, perm)) return true;
		sender.sendMessage("[Pic] You don't have permission.");
		return false;
	}
	
}

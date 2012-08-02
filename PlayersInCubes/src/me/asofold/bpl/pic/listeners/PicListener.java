package me.asofold.bpl.pic.listeners;

import me.asofold.bpl.pic.core.PicCore;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public final class PicListener implements Listener {
	
	private final PicCore core;
	
	public PicListener(PicCore core){
		this.core = core;
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	final void move(final PlayerMoveEvent event){
		final Location to = event.getTo();
		if (to !=null) core.check(event.getPlayer(), to);
		else{
			final Location from = event.getFrom();
			if (from != null) core.check(event.getPlayer(), from);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	final void teleport(final PlayerTeleportEvent event){
		final Location to = event.getTo();
		if (to !=null) core.check(event.getPlayer(), to);
		else{
			final Location from = event.getFrom();
			if (from != null) core.check(event.getPlayer(), from);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	final void join(final PlayerJoinEvent event){
		final Player player = event.getPlayer();
		core.checkIn(player, player.getLocation());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	final void quit(final PlayerQuitEvent event){
		core.onQuit(event.getPlayer()); //core.checkOut(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	final void kick(final PlayerKickEvent event){
		core.onQuit(event.getPlayer()); //core.checkOut(event.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	final void death(final EntityDeathEvent event){
		final Entity entity = event.getEntity();
		if (entity instanceof Player) core.checkOut((Player) entity);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	final void respawn(final PlayerRespawnEvent event){
		core.checkIn(event.getPlayer(), event.getRespawnLocation());
	}
}

package me.ponktacology.clashmc.sector.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


@RequiredArgsConstructor
public class PlayerMoveListener implements Listener {


  private final PlayerTeleportUpdater teleportUpdater;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerMoveEvent( PlayerMoveEvent event) {
    Player player = event.getPlayer();
    Location from = event.getFrom();
    Location to = event.getTo();

    if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
      return;
    }

    if (this.teleportUpdater.isBeingTeleported(player)) {
      this.teleportUpdater.setBeingTeleported(player, false);
    }
  }
}

package me.ponktacology.clashmc.sector.player.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;


@RequiredArgsConstructor
public class PlayerJumpPadListener implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerMoveEvent( PlayerMoveEvent event) {
    Player player = event.getPlayer();
    Location from = event.getFrom();
    Location to = event.getTo();

    if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
      return;
    }

    if (to.getBlock().getType() == Material.GOLD_PLATE) {
      Vector v = player.getLocation().getDirection().multiply(3.0D).setY(1.0D);
      player.setVelocity(v);
    }
  }
}

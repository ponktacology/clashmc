package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@RequiredArgsConstructor
public class PlayerRespawnListener implements Listener {

  private final Location spawnLocation =
      new Location(Bukkit.getWorld("world"), 0, 100, 0, 90.0F, 0F);

  @SneakyThrows
  @EventHandler
  public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
 //   Player player = event.getPlayer();
  //  event.setRespawnLocation(spawnLocation);
  }
}

package me.ponktacology.clashmc.farmer.farmer.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.farmer.player.FarmerPlayer;
import me.ponktacology.clashmc.farmer.player.cache.FarmerPlayerCache;
import me.ponktacology.clashmc.farmer.player.factory.FarmerPlayerFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final FarmerPlayerCache playerCache;
  private final FarmerPlayerFactory playerFactory;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    FarmerPlayer farmerPlayer = this.playerFactory.create(player.getUniqueId(), player.getName());

    this.playerCache.add(farmerPlayer);
  }

  @EventHandler
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.playerCache.remove(player);
  }
}

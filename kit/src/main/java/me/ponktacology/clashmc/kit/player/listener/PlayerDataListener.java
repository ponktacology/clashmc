package me.ponktacology.clashmc.kit.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.kit.player.KitPlayer;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;
import me.ponktacology.clashmc.kit.player.factory.KitPlayerFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
@Slf4j
public class PlayerDataListener implements Listener {

  private final TaskDispatcher taskDispatcher;
  private final KitPlayerCache playerCache;
  private final KitPlayerFactory playerFactory;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    try {
      KitPlayer kitPlayer = this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());
      this.playerCache.add(kitPlayer);
    } catch (Exception e) {
      log.info("(Kits) Player " + player.getName() + " failed to load.");
      event.disallow("&cNie udało się załadować twoich rang, skontaktuj się z administracją.");
    }
  }

  @EventHandler
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.playerCache.remove(player).ifPresent(BukkitPlayerWrapper::save);
  }
}

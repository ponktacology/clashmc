package me.ponktacology.clashmc.safe.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.safe.player.SafePlayer;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;
import me.ponktacology.clashmc.safe.player.factory.SafePlayerFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Slf4j
@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

  private final TaskDispatcher taskDispatcher;
  private final SafePlayerCache playerCache;
  private final SafePlayerFactory playerFactory;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    try {
      SafePlayer safePlayer =
          this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());
      this.playerCache.add(safePlayer);
    } catch (Exception e) {
      log.info("(Safes) Player " + player.getName() + " failed to load.");
      event.disallow("&cNie udało się załadować twoich rang, skontaktuj się z administracją.");
    }
  }

  @EventHandler
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    this.playerCache.remove(event.getPlayer()).ifPresent(BukkitPlayerWrapper::save);
  }
}

package me.ponktacology.clashmc.crate.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.crate.player.CratePlayer;
import me.ponktacology.clashmc.crate.player.cache.CratePlayerCache;
import me.ponktacology.clashmc.crate.player.factory.CratePlayerFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Slf4j
@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final TaskDispatcher taskDispatcher;
  private final CratePlayerCache playerCache;
  private final CratePlayerFactory playerFactory;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    try {
      CratePlayer cratePlayer =
          this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());
      this.playerCache.add(cratePlayer);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("(Crates) Player " + player.getName() + " failed to load.");
      event.disallow("&cNie udało się załadować twoich rang, skontaktuj się z administracją.");
    }
  }

  @EventHandler
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    this.playerCache.remove(event.getPlayer()).ifPresent(BukkitPlayerWrapper::save);
  }
}

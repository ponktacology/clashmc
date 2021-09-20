package me.ponktacology.clashmc.drop.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.player.factory.DropPlayerFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Slf4j
@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final TaskDispatcher taskDispatcher;
  private final DropPlayerCache playerCache;
  private final DropPlayerFactory playerFactory;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    try {
      DropPlayer dropPlayer =
          this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());
      this.playerCache.add(dropPlayer);
    } catch (Exception e) {
      log.info("(Drop) Player " + player.getName() + " failed to load.");
      event.disallow("&cNie udało się załadować twoich danych, skontaktuj się z administracją.");
    }
  }

  @EventHandler
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.playerCache.remove(player).ifPresent(BukkitPlayerWrapper::save);
  }
}

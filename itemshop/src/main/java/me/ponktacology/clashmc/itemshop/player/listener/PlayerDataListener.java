package me.ponktacology.clashmc.itemshop.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import me.ponktacology.clashmc.itemshop.player.factory.ItemShopPlayerFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final TaskDispatcher taskDispatcher;
  private final ItemShopPlayerCache playerCache;
  private final ItemShopPlayerFactory playerFactory;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    ItemShopPlayer itemShopPlayer =
        this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());
    this.playerCache.add(itemShopPlayer);
    this.taskDispatcher.runLaterAsync(itemShopPlayer::initRanks, 10L, TimeUnit.SECONDS);
  }

  @EventHandler
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.playerCache.remove(player).ifPresent(BukkitPlayerWrapper::save);
  }
}

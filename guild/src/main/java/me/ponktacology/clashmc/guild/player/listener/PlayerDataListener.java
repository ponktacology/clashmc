package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.guild.guild.updater.GuildCuboidUpdater;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.factory.GuildPlayerFactory;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.player.transfer.event.AsyncPlayerSectorTransferEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;
  private final PlayerTransferUpdater transferUpdater;
  private final GuildPlayerFactory playerFactory;
  private final GuildCuboidUpdater cuboidUpdater;

  private final TaskDispatcher taskDispatcher;
  private final Location spawnLocation =
      new Location(Bukkit.getWorld("world"), 0, 100, 0, 90.0F, 0F);

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    try {
      GuildPlayer guildPlayer =
          this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());

      this.playerCache.add(guildPlayer);
      this.cuboidUpdater.update(guildPlayer);
    } catch (Exception e) {
      log.info("(Guilds) Player " + player.getName() + " failed to load.");
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onPlayerSectorLeave(AsyncPlayerSectorTransferEvent event) {
    this.playerCache
        .get(event.getPlayer())
        .ifPresent(
            it -> {
              it.addPlayTime();
              this.taskDispatcher.runAsync(it::save);
            });
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuitEventLowest(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    this.playerCache
        .get(player)
        .ifPresent(
            it -> {
              if (it.hasCombatTag()) {

                Optional<CorePlayer> corePlayerOptional = this.corePlayerCache.get(player);

                if (corePlayerOptional.isPresent()) {
                  if (corePlayerOptional.get().isStaff()) return;
                }

                player.setHealth(0.0);
                player.spigot().respawn();
              }
            });
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerKickEvent(PlayerKickEvent event) {
    if (event.getReason().equals("disconnect.spam")) return;

    this.playerCache.get(event.getPlayer()).ifPresent(GuildPlayer::disableCombatTag);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.playerCache
        .remove(player)
        .ifPresent(
            it -> {
              if (!this.transferUpdater.isBeingTransferred(player)) {
                it.addPlayTime();

                it.save();
              }
            });
  }
}

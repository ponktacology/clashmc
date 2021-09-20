package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;


@RequiredArgsConstructor
public class PlayerTeleportListener implements Listener {


  private final GuildPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerTeleportEvent( PlayerTeleportEvent event) {
    if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

    Player player = event.getPlayer();

    this.playerCache
        .get(player)
        .ifPresent(
            it -> {
              it.increaseUsedPearls();

              this.taskDispatcher.runAsync(it::save);
            });

    event.setCancelled(true);
    player.teleport(event.getTo());
    player.damage(1);
  }
}

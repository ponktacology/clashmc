package me.ponktacology.clashmc.queue.entry.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.queue.entry.QueueEntry;
import me.ponktacology.clashmc.queue.entry.cache.QueueEntryCache;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private final Location spawnLocation =
      new Location(Bukkit.getWorld("world_the_end"), 0.0, 1000.0, 0.0, 0F, 0F);
  private final TaskDispatcher taskDispatcher;
  private final QueueEntryCache entryCache;
  private final CorePlayerCache playerCache;

  @EventHandler
  public void onEntityDamageEvent(EntityDamageEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
    event.setFoodLevel(20);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Bukkit.getOnlinePlayers()
        .forEach(
            it -> {
              player.hidePlayer(it);
              it.hidePlayer(player);
            });

    player.setFoodLevel(20);
    player.teleport(this.spawnLocation);
    player.setAllowFlight(true);
    player.setFlying(true);
    player.setWalkSpeed(0F);
    player.setFlySpeed(0F);

    this.taskDispatcher.runLaterAsync(
        () -> {
          if (Bukkit.getPlayer(player.getUniqueId()) == null) return;
          CorePlayer corePlayer = this.playerCache.getOrKick(player);
          this.entryCache.add(new QueueEntry(corePlayer));
          player.sendMessage(Text.colored("&aAutomatycznie dodano do kolejki."));
        },
        1,
        TimeUnit.SECONDS);
  }

  @EventHandler
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    Player player = event.getPlayer();
    this.entryCache.remove(player.getUniqueId());
  }

  @EventHandler
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) return;

    if (this.spawnLocation.distance(event.getTo()) > 0) {
      event.getPlayer().teleport(event.getFrom());
    }
  }
}

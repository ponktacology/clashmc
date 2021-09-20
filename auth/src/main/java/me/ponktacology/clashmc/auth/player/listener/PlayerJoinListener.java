package me.ponktacology.clashmc.auth.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.auth.player.AuthPlayer;
import me.ponktacology.clashmc.auth.player.cache.AuthPlayerCache;
import me.ponktacology.clashmc.auth.player.factory.AuthPlayerFactory;
import me.ponktacology.clashmc.auth.util.BungeeUtil;
import me.ponktacology.clashmc.core.util.Text;
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
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class PlayerJoinListener implements Listener {

  private final Location spawnLocation =
      new Location(Bukkit.getWorld("world_the_end"), 0.0, 1000.0, 0.0, 0F, 0F);
  private final AuthPlayerFactory playerFactory;
  private final AuthPlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;

  @EventHandler
  public void onEntityDamageEvent(EntityDamageEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
    event.setFoodLevel(20);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    Bukkit.getOnlinePlayers()
        .forEach(
            it -> {
              player.hidePlayer(it);
              it.hidePlayer(player);
            });

    player.teleport(this.spawnLocation);
    player.setAllowFlight(true);
    player.setFlying(true);
    player.setWalkSpeed(0F);
    player.setFlySpeed(0F);
    player.setFoodLevel(20);

    try {
      this.taskDispatcher.runAsync(
          () -> {
            AuthPlayer authPlayer =
                this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());

            if (authPlayer.isLoggedIn()) {
              this.taskDispatcher.runLaterAsync(
                  () -> {
                    player.sendMessage(
                        Text.colored(
                            "&aAutomatycznie zalogowano.\n&eZostaniesz teraz przeniesiony na lobby."));
                    BungeeUtil.sendToServer(player, "lobby");
                  },
                  2L,
                  TimeUnit.SECONDS);
              return;
            }

            if (authPlayer.isRegistered()) {
              player.sendMessage(
                  Text.colored("&eZaloguj się używając komendy &f/login <hasło>&e."));
            } else {
              player.sendMessage(
                  Text.colored(
                      "&eZarejestruj się używając komendy &f/register <hasło> <hasło>&e."));
            }

            this.playerCache.add(authPlayer);
          });
    } catch (Exception e) {
      log.info("AuthPlayer not loaded properly player= " + player.getName());
      e.printStackTrace();
      player.kickPlayer(
          Text.colored("&cNie udało się załadować twoich danych, skontaktuj się z administracją."));
    }
  }

  @EventHandler
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.playerCache.remove(player).ifPresent(it -> this.taskDispatcher.runAsync(it::save));
  }

  @EventHandler
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) return;

    if (this.spawnLocation.distance(event.getTo()) > 0) {
      event.getPlayer().teleport(event.getFrom());
    }
  }
}

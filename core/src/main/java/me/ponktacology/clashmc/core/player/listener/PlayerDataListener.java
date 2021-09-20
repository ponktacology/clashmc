package me.ponktacology.clashmc.core.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.blazingpack.binds.BindsHelper;
import me.ponktacology.clashmc.core.blazingpack.tab.TabHelper;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
public class PlayerDataListener implements Listener {

  private final CorePlayerFactory playerFactory;
  private final CorePlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;
  private final String serverName = CorePlugin.INSTANCE.getConfiguration().getServerName();

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLoginEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    AsyncPlayerJoinEvent asyncPlayerJoinEvent = new AsyncPlayerJoinEvent(player);
    CompletableFuture.supplyAsync(
            () -> {
              Bukkit.getPluginManager().callEvent(asyncPlayerJoinEvent);
              return asyncPlayerJoinEvent;
            })
        .thenAccept(
            e -> {
              if (e.isCancelled()) {
                player.kickPlayer(Text.colored(e.getCancelMessage()));
              }
            });
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    try {
      CorePlayer corePlayer =
          this.playerFactory.loadOrCreate(
              player.getUniqueId(),
              player.getName(),
              player.getAddress().toString().replace("/", ""));

      Optional<Punishment> punishmentOptional =
          corePlayer.getActivePunishment(PunishmentType.BLACKLIST);

      if (!punishmentOptional.isPresent()) {
        punishmentOptional = corePlayer.getActivePunishment(PunishmentType.BAN);
      }

      if (!punishmentOptional.isPresent()) {
        for (CorePlayer alt : corePlayer.getAlts()) {
          punishmentOptional = alt.getActivePunishment(PunishmentType.BLACKLIST);
          if (punishmentOptional.isPresent()) break;

          punishmentOptional = alt.getActivePunishment(PunishmentType.BAN);
          if (punishmentOptional.isPresent()) break;
        }
      }

      if (punishmentOptional.isPresent()) {
        Punishment punishment = punishmentOptional.get();
        event.disallow(
            "&cJesteś "
                + (punishment.getDuration() > 0 ? "" : "permanentnie ")
                + punishment.getType().getAddFormat()
                + "!"
                + "\n\n&cPowód: &f"
                + punishment.getReason()
                + (punishment.getDuration() > 0
                    ? "\n&cWygasa: &f"
                        + TimeUtil.formatTimeMillisToDate(
                            punishment.getAddedOn() + punishment.getDuration())
                    : "")
                + "\n\n&c&oMożesz odwołać się od kary na Team Speaku: ts.clashmc.pl.");
        return;
      }

      this.playerCache.add(corePlayer);

      BindsHelper.send(player);
      TabHelper.send(player);

      this.taskDispatcher.run(
          () -> {
            corePlayer.recalculatePermissions();

            if (!(serverName.equals("auth") || serverName.equals("lobby"))) {
              corePlayer.setupVanish();
            }
          });
    } catch (Exception e) {
      log.info("(Core) Player " + player.getName() + " failed to load.");
      e.printStackTrace();
      event.disallow("&cNie udało się załadować twoich rang, skontaktuj się z administracją.");
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    AsyncPlayerQuitEvent asyncPlayerQuitEvent = new AsyncPlayerQuitEvent(player);
    this.taskDispatcher.runAsync(
        () -> {
          Bukkit.getPluginManager().callEvent(asyncPlayerQuitEvent);
        });
  }

  @EventHandler
  public void onAsyncPlayerQuit(AsyncPlayerQuitEvent event) {
    this.playerCache
        .remove(event.getPlayer())
        .ifPresent(it -> this.taskDispatcher.runAsync(it::save));
  }
}

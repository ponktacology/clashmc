package me.ponktacology.clashmc.core.player.listener;

import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.util.Timer;
import me.ponktacology.clashmc.core.CoreConstants;
import me.ponktacology.clashmc.core.log.command.CommandLog;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class PlayerCommandPreprocessListener implements Listener {

  private final CorePlayerCache playerCache;

  public PlayerCommandPreprocessListener(CorePlayerCache playerCache) {
    this.playerCache = playerCache;
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerCommandPreprocessEventLowest( PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();

    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    if (corePlayer.isStaff()) {
      return;
    }

    if (Arrays.stream(CoreConstants.BLOCKED_COMMANDS)
        .anyMatch(it -> event.getMessage().toLowerCase(Locale.ROOT).startsWith(it))) {
      player.sendMessage(
          Text.colored("&cNie znaleziono takiej komendy. Wszystkie komendy pod &7/pomoc&c."));
      event.setCancelled(true);
      return;
    }

    Timer commandTimer = corePlayer.getCommandTimer();

    if (!commandTimer.hasPassed()) {
      player.sendMessage(
          Text.colored("&cMusisz odczekać chwilę, zanim użyjesz kolejną komendę."));
      event.setCancelled(true);
      return;
    }

    commandTimer.reset();
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerCommandPreprocessEvent( PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();

    Optional<CorePlayer> playerOptional = playerCache.get(player);

    if (!playerOptional.isPresent()) {
      log.info("Player used command but not in cache, player= " + player.getName());
      event.setCancelled(true);
      return;
    }

    CorePlayer corePlayer = playerOptional.get();

    corePlayer.addCommandLog(new CommandLog(event.getMessage()));
  }
}

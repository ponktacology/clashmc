package me.ponktacology.clashmc.auth.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


import java.util.Optional;

@RequiredArgsConstructor
public class PlayerChatListener implements Listener {


  private final CorePlayerCache playerCache;

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerChatEvent( AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();

    Optional<CorePlayer> corePlayerOptional = this.playerCache.get(player);

    if (!corePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer corePlayer = corePlayerOptional.get();

    if (!corePlayer.isStaff()) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onPlayerCommandPreprocessEvent( PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();

    Optional<CorePlayer> corePlayerOptional = this.playerCache.get(player);

    if (!corePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer corePlayer = corePlayerOptional.get();

    if (!corePlayer.isStaff()
        && !(event.getMessage().startsWith("/login")
            || event.getMessage().startsWith("/register"))) {
      event.setCancelled(true);
    }
  }
}

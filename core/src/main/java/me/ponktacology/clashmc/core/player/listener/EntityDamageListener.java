package me.ponktacology.clashmc.core.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@RequiredArgsConstructor
public class EntityDamageListener implements Listener {

  private final CorePlayerCache playerCache;

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onEntityDamageEvent( EntityDamageEvent event) {
    Entity entity = event.getEntity();

    if (!(entity instanceof Player)) {
      return;
    }

    Player player = (Player) entity;
    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    if (corePlayer.isGod()) {
      event.setCancelled(true);
    }
  }
}

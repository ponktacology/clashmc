package me.ponktacology.clashmc.core.player.event;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class AsyncPlayerJoinEvent extends BaseCancellableEvent {
  private final Player player;
  private String cancelMessage;

  public void disallow(String message) {
    this.cancelMessage = message;
    this.setCancelled(true);
  }
}

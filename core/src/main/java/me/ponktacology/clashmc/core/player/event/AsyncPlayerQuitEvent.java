package me.ponktacology.clashmc.core.player.event;

import lombok.Data;
import org.bukkit.entity.Player;

@Data
public class AsyncPlayerQuitEvent extends BaseCancellableEvent {
  private final Player player;
}

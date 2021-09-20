package me.ponktacology.clashmc.core.player.event;

import org.bukkit.event.Cancellable;

public class BaseCancellableEvent extends BaseEvent implements Cancellable {

  private boolean cancelled;

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(boolean b) {
    this.cancelled = b;
  }
}

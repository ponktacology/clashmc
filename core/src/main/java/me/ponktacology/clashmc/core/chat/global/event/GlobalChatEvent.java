package me.ponktacology.clashmc.core.chat.global.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Getter
@Setter
public class GlobalChatEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final Player player;
  private String message;
  private String format;
  private boolean cancelled;

  public GlobalChatEvent(Player player, String message, String format) {
    this.player = player;
    this.message = message;
    this.format = format;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean b) {
    this.cancelled = b;
  }


  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }


  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}

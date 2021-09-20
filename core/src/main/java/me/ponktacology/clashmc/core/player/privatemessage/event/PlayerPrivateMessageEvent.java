package me.ponktacology.clashmc.core.player.privatemessage.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.core.player.CorePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Getter
@Setter
@RequiredArgsConstructor
public class PlayerPrivateMessageEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final CorePlayer sender;
  private final CorePlayer receiver;
  private String message;

  public PlayerPrivateMessageEvent(CorePlayer sender, CorePlayer receiver, String message) {
    this.sender = sender;
    this.receiver = receiver;
    this.message = message;
  }

  private boolean cancelled;

  @Override
  public boolean isCancelled() {
    return this.cancelled;
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

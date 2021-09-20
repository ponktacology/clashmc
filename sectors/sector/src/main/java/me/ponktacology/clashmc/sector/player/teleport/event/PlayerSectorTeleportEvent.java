package me.ponktacology.clashmc.sector.player.teleport.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Getter
@RequiredArgsConstructor
public class PlayerSectorTeleportEvent extends Event implements Cancellable {

  private static final HandlerList HANDLER_LIST = new HandlerList();


  private final Player player;

  private final Sector sector;

  private final Location location;

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

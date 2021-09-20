package me.ponktacology.clashmc.core.player.death;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Data
public class PlayerRankDeathEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();


  private final Player player;
  private final int rankChange;


  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }


  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}

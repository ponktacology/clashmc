package me.ponktacology.clashmc.core.chat.staff;

import lombok.Data;
import me.ponktacology.clashmc.core.player.CorePlayer;


@Data
public class StaffMessage {
  
  private final CorePlayer sender;
  
  private final String message;
}

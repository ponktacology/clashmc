package me.ponktacology.clashmc.core.player.privatemessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.ponktacology.clashmc.core.player.CorePlayer;


@Data
@AllArgsConstructor
public class PrivateMessage {
  
  private final CorePlayer sender;
  
  private final CorePlayer receiver;
  private String message;
}

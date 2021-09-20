package me.ponktacology.clashmc.core.chat.global.broadcast;

import lombok.Data;
import me.ponktacology.clashmc.core.chat.global.broadcast.type.BroadcastType;


@Data
public class Broadcast {


  private final String message;

  private final BroadcastType type;
  private final int fadeIn, duration, fadeOut;
}

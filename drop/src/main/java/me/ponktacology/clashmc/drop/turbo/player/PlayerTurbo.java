package me.ponktacology.clashmc.drop.turbo.player;

import lombok.Data;
import me.ponktacology.clashmc.drop.player.DropPlayer;


@Data
public class PlayerTurbo {
  
  private final DropPlayer player;
  private final boolean isTurboDrop;
  private final long duration;
}

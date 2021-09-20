package me.ponktacology.clashmc.crate.crate.opening;

import lombok.Data;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.item.CrateItem;
import me.ponktacology.clashmc.crate.player.CratePlayer;


@Data
public class CrateOpening {

  private final CratePlayer player;
  private final Crate crate;
  private final CrateItem reward;
}

package me.ponktacology.clashmc.crate.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.crate.player.CratePlayer;

public class CratePlayerFactory extends BukkitPlayerFactory<CratePlayer> {

  public CratePlayerFactory() {
    super(CratePlayer.class, CratePlayer::new);
  }
}

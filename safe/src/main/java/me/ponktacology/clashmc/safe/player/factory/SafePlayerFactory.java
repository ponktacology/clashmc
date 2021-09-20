package me.ponktacology.clashmc.safe.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.safe.player.SafePlayer;

public class SafePlayerFactory extends BukkitPlayerFactory<SafePlayer> {

  public SafePlayerFactory() {
    super(SafePlayer.class, SafePlayer::new);
  }
}

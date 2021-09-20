package me.ponktacology.clashmc.kit.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.kit.player.KitPlayer;

public class KitPlayerFactory extends BukkitPlayerFactory<KitPlayer> {

  public KitPlayerFactory() {
    super(KitPlayer.class, KitPlayer::new);
  }
}

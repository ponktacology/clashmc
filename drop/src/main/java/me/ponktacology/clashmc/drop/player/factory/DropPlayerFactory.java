package me.ponktacology.clashmc.drop.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.drop.player.DropPlayer;

public class DropPlayerFactory extends BukkitPlayerFactory<DropPlayer> {

  public DropPlayerFactory() {
    super(DropPlayer.class, DropPlayer::new);
  }
}

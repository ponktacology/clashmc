package me.ponktacology.clashmc.drop.player.cache;

import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.drop.player.DropPlayer;

public class DropPlayerCache extends BukkitPlayerCache<DropPlayer> {

  public DropPlayerCache(BukkitPlayerFactory<DropPlayer> playerFactory) {
    super(playerFactory);
  }
}

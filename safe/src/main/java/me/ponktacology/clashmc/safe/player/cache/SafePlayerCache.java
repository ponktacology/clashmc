package me.ponktacology.clashmc.safe.player.cache;

import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.safe.player.SafePlayer;

public class SafePlayerCache extends BukkitPlayerCache<SafePlayer> {

  public SafePlayerCache(BukkitPlayerFactory<SafePlayer> playerFactory) {
    super(playerFactory);
  }
}

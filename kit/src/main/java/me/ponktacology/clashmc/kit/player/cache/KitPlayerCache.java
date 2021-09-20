package me.ponktacology.clashmc.kit.player.cache;

import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.kit.player.KitPlayer;

public class KitPlayerCache extends BukkitPlayerCache<KitPlayer> {
  public KitPlayerCache(BukkitPlayerFactory<KitPlayer> playerFactory) {
    super(playerFactory);
  }
}

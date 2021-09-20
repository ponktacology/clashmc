package me.ponktacology.clashmc.crate.player.cache;

import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.crate.player.CratePlayer;

public class CratePlayerCache extends BukkitPlayerCache<CratePlayer> {

  public CratePlayerCache(BukkitPlayerFactory<CratePlayer> playerFactory) {
    super(playerFactory);
  }
}

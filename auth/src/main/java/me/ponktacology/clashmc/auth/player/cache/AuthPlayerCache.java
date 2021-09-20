package me.ponktacology.clashmc.auth.player.cache;

import me.ponktacology.clashmc.auth.player.AuthPlayer;
import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;


public class AuthPlayerCache extends BukkitPlayerCache<AuthPlayer> {
  public AuthPlayerCache(BukkitPlayerFactory<AuthPlayer> playerFactory) {
    super(playerFactory);
  }
}

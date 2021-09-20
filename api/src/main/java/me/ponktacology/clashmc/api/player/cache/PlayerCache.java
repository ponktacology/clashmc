package me.ponktacology.clashmc.api.player.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.api.player.PlayerWrapper;


import java.util.Optional;
import java.util.UUID;

public abstract class PlayerCache<P extends PlayerWrapper> extends KeyValueCache<UUID, P> {

  public void add( P player) {
    super.add(player.getUuid(), player);
  }

  public Optional<P> remove( P player) {
    return super.remove(player.getUuid());
  }

  public abstract Optional<P> get(String name);
}

package me.ponktacology.clashmc.core.player.cache;

import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;


import java.util.Set;
import java.util.stream.Collectors;

public class CorePlayerCache extends BukkitPlayerCache<CorePlayer> {
  public CorePlayerCache(CorePlayerFactory playerFactory) {
    super(playerFactory);
  }

  @Override
  public void add( CorePlayer player) {
    super.add(player);
    super.updateCurrentServer(player);
    super.cache(player);
  }

  public Set<CorePlayer> staffMembers() {
    return this.values().stream().filter(CorePlayer::isStaff).collect(Collectors.toSet());
  }
}

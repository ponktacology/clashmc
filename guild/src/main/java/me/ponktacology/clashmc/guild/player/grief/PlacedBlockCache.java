package me.ponktacology.clashmc.guild.player.grief;

import me.ponktacology.clashmc.api.cache.ValueCache;

public class PlacedBlockCache extends ValueCache<PlacedBlock> {

  public PlacedBlockCache() {
    super(false);
  }

  public void clearAll() {
    for (PlacedBlock value : this.values()) {
      value.clear();
    }
  }
}

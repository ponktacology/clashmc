package me.ponktacology.clashmc.kit.kit.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.kit.kit.Kit;


import java.util.List;

public class KitCache extends IgnoreCaseKeyValueCache<Kit> {

  public void add( Kit kit) {
    super.add(kit.getName(), kit);
  }

  public void addAll( List<Kit> kits) {
    kits.forEach(this::add);
  }
}

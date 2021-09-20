package me.ponktacology.clashmc.safe.item.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.safe.item.SafeItem;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SafeItemCache extends KeyValueCache<UUID, SafeItem> {

  public void add( SafeItem item) {
    super.add(item.getUuid(), item);
  }

  public void addAll( List<SafeItem> items) {
    items.forEach(this::add);
  }


  public Optional<SafeItem> get(String name) {
    return this.values().stream().filter(it -> it.getRawName().equalsIgnoreCase(name)).findFirst();
  }
}

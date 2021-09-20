package me.ponktacology.clashmc.drop.item.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.player.DropPlayer;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DropItemCache extends IgnoreCaseKeyValueCache<DropItem> {

  public void add( DropItem item) {
    super.add(item.getName(), item);
  }

  public void addAll( List<DropItem> items) {
    items.forEach(this::add);
  }

  public void remove( DropItem item) {
    super.remove(item.getName());
  }

  public Optional<DropItem> getRandom( DropPlayer dropPlayer, DropType type) {
    Set<DropItem> items = this.values(type);
    double sum = items.stream().mapToDouble(dropItem -> dropItem.getChance(dropPlayer)).sum();
    double rand = Math.random() * (type == DropType.COBBLEX ? sum : 100);
    DropItem randomItem = null;

    for (DropItem e : items) {
      rand -= e.getChance(dropPlayer);

      if (rand <= 0) {
        randomItem = e;
        break;
      }
    }

    return Optional.ofNullable(randomItem);
  }

  public Set<DropItem> values(DropType type) {
    return this.values().stream()
        .filter(it -> it.getType().equals(type))
        .collect(Collectors.toSet());
  }
}

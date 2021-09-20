package me.ponktacology.clashmc.sector.border.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.sector.border.Border;
import org.bukkit.entity.Player;


import java.util.Optional;
import java.util.UUID;

public class BorderCache extends KeyValueCache<UUID, Border> {

  public Optional<Border> get( Player player) {
    return super.get(player.getUniqueId());
  }

  public void add( Border border) {
    super.add(border.getTargetPlayer(), border);
  }

  public void remove( Player player) {
    super.remove(player.getUniqueId());
  }

  public void update( Player player) {
    if (!this.cache().containsKey(player.getUniqueId())) return;
    get(player).ifPresent(it -> it.update(player));
  }
}

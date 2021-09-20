package me.ponktacology.clashmc.crate.crate.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.core.util.ItemUtil;
import me.ponktacology.clashmc.crate.crate.Crate;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class CrateCache extends IgnoreCaseKeyValueCache<Crate> {

  public void add(Crate crate) {
    super.add(crate.getName(), crate);
  }

  public void addAll(List<Crate> crates) {
    crates.forEach(this::add);
  }

  public Optional<Crate> get(ItemStack itemStack) {
    for (Crate it : this.values()) {
      if (ItemUtil.isSimilar(itemStack, it.getCrateItem())) {
        return Optional.of(it);
      }
    }
    return Optional.empty();
  }
}

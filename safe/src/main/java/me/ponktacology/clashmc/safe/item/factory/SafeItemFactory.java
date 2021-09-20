package me.ponktacology.clashmc.safe.item.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.safe.item.SafeItem;
import org.bukkit.inventory.ItemStack;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SafeItemFactory implements Factory<SafeItem> {


  private final DataService dataService;


  public SafeItem create(String name, ItemStack item) {
    return new SafeItem(name, item);
  }

  public Optional<SafeItem> load( UUID uuid) {
    return this.dataService.load(uuid.toString(), SafeItem.class);
  }


  public List<SafeItem> loadAll() {
    return this.dataService.loadAll(SafeItem.class);
  }

  public void delete(SafeItem safeItem) {
    this.dataService.delete(safeItem);
  }
}

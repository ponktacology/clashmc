package me.ponktacology.clashmc.drop.item.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.drop.item.DropItem;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DropItemFactory implements Factory<DropItem> {


  private final DataService dataService;


  public DropItem create(String name) {
    return new DropItem(name);
  }

  public Optional<DropItem> load(String name) {
    return this.dataService.load(name, DropItem.class);
  }


  public List<DropItem> loadAll() {
    return this.dataService.loadAll(DropItem.class);
  }

  public void delete(DropItem item) {
    this.dataService.delete(item);
  }
}

package me.ponktacology.clashmc.kit.kit.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.kit.kit.Kit;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class KitFactory implements Factory<Kit> {

  
  private final DataService dataService;

  
  public Kit create(String name) {
    return new Kit(name);
  }

  public Optional<Kit> load(String name) {
    return this.dataService.load(name, Kit.class);
  }

  
  public List<Kit> loadAll() {
    return this.dataService.loadAll(Kit.class);
  }

  public void delete(Kit kit) {
    this.dataService.delete(kit);
  }
}

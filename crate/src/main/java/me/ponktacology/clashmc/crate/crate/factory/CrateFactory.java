package me.ponktacology.clashmc.crate.crate.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.crate.crate.Crate;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CrateFactory implements Factory<Crate> {


  private final DataService dataService;


  public Crate create(String name) {
    return new Crate(name);
  }

  public Optional<Crate> load(String name) {
    return this.dataService.load(name, Crate.class);
  }


  public List<Crate> loadAll() {
    return this.dataService.loadAll(Crate.class);
  }

  public void delete(Crate safeItem) {
    this.dataService.delete(safeItem);
  }
}

package me.ponktacology.clashmc.drop.generator.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.drop.generator.Generator;
import org.bukkit.Location;


import java.util.List;

@RequiredArgsConstructor
public class GeneratorFactory implements Factory<Generator> {


  private final DataService dataService;


  public Generator create(Location location) {
    return new Generator(location);
  }


  public List<Generator> loadAll() {
    return this.dataService.loadAll(Generator.class);
  }

  public void delete(Generator generator) {
    this.dataService.delete(generator);
  }
}

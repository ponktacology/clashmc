package me.ponktacology.clashmc.drop.generator.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.drop.generator.Generator;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;

import java.util.List;

public class GeneratorCache extends KeyValueCache<Location, Generator> {

  private final Sector localSector = SectorPlugin.INSTANCE.getLocalSector();

  public GeneratorCache() {
    super(false);
  }

  public void add(Generator generator) {
    super.add(generator.getLocation(), generator);
  }

  public void addAll(List<Generator> generators) {
    generators.stream()
        .filter(it -> RegionUtil.isIn(it.getLocation(), this.localSector))
        .forEach(this::add);
  }
}

package me.ponktacology.clashmc.sector.api.sector.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SectorCache extends IgnoreCaseKeyValueCache<Sector> {

  public void add(Sector sector) {
    this.add(sector.getName(), sector);
  }

  @Override
  public Optional<Sector> get(String key) {
    Optional<Sector> sectorOptional = super.get(key);

    if (sectorOptional.isPresent()) {
      Sector sector = sectorOptional.get();

      if (sector.isAvailable()) return sectorOptional;
    }

    return Optional.empty();
  }

  public Optional<Sector> getRandom(SectorType type) {
    Sector[] sectors = this.values(type).toArray(new Sector[0]);

    if (sectors.length == 0) {
      return Optional.empty();
    }

    return Optional.of(sectors[(int) (ThreadLocalRandom.current().nextFloat() * sectors.length)]);
  }

  public Optional<Sector> getLeastCrowded(SectorType type) {
    Set<Sector> sectors = this.values(type);

    if (sectors.isEmpty()) {
      return Optional.empty();
    }

    return sectors.stream().min(Comparator.comparingInt(o -> o.getData().getPlayers()));
  }

  public TreeSet<Sector> sortedValues(SectorType type) {
    TreeSet<Sector> sectors = new TreeSet<>(Comparator.comparing(Sector::getName));
    sectors.addAll(this.values(type));
    return sectors;
  }

  public Set<Sector> values(SectorType type) {
    return this.values().stream().filter(it -> it.getType() == type).collect(Collectors.toSet());
  }

  @Override
  public Set<Sector> values() {
    return super.values().stream().filter(Sector::isAvailable).collect(Collectors.toSet());
  }

  public TreeSet<Sector> sortedValues() {
    TreeSet<Sector> sectors = new TreeSet<>(Comparator.comparing(Sector::getName));
    sectors.addAll(this.values());
    return sectors;
  }

  public int getSpecialPlayersCount() {
    return this.values().stream()
        .filter(it -> it.getType().isSpecial())
        .mapToInt(it -> it.getData().getPlayers())
        .sum();
  }

  public int getMaskedPlayersCount() {
    return Math.min(2000, this.getNormalPlayersCount());
  }

  public int getNormalPlayersCount() {
    return this.values().stream()
        .filter(it -> !it.isSpecial())
        .mapToInt(it -> it.getOnlinePlayers())
        .sum();
  }

  public int getGlobalMaxPlayers() {
    return this.values().stream().mapToInt(it -> it.getData().getMaxPlayers()).sum();
  }

  public double getAverageGlobalCPUUsage() {
    return MathUtil.roundOff(
        this.values().stream().mapToDouble(it -> it.getData().getCpuUsage()).average().orElse(-1.0)
            * 100,
        3);
  }

  public double getAverageGlobalTPS() {
    return MathUtil.roundOff(
        this.values().stream()
            .mapToDouble(
                it -> Arrays.stream(it.getData().getTps()).summaryStatistics().getAverage())
            .average()
            .orElse(-1.0),
        2);
  }
}

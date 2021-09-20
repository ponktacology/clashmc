package me.ponktacology.clashmc.farmer.farmer.filler.region;

import com.google.common.collect.Lists;
import me.ponktacology.clashmc.api.Region;
import me.ponktacology.clashmc.farmer.FarmerConstants;
import me.ponktacology.clashmc.farmer.farmer.filler.pillar.Pillar;
import org.bukkit.Location;

import java.util.Collections;
import java.util.List;

public class FarmerRegion extends Region {

  private final List<Pillar> pillars = Lists.newArrayList();

  public FarmerRegion(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
    super(minX, maxX, minY, maxY, minZ, maxZ);

    this.cachePillars();
  }

  public static FarmerRegion fromCorners(Location firstCorner, Location secondCorner) {
    int firstX = firstCorner.getBlockX();
    int secondX = secondCorner.getBlockX();

    int firstZ = firstCorner.getBlockZ();
    int secondZ = secondCorner.getBlockZ();

    int firstY = firstCorner.getBlockY();
    int secondY = secondCorner.getBlockY();

    return new FarmerRegion(
        Math.min(firstX, secondX),
        Math.max(firstX, secondX),
        Math.min(firstY, secondY),
        Math.max(firstY, secondY),
        Math.min(firstZ, secondZ),
        Math.max(firstZ, secondZ));
  }

  public int price() {
    return super.area() * FarmerConstants.PRICE_PER_PILLAR_FARMER_REGION;
  }

  public List<Pillar> pillars() {
    return Collections.unmodifiableList(this.pillars);
  }

  public Pillar pillar(int index) {
    return this.pillars.get(index);
  }

  private void cachePillars() {
    this.pillars.clear();

    for (int x = getMinX(); x <= getMaxX(); x++) {
      for (int z = getMinZ(); z <= getMaxZ(); z++) {
        this.pillars.add(new Pillar(x, z));
      }
    }
  }
}

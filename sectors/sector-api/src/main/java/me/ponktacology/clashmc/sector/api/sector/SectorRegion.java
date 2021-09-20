package me.ponktacology.clashmc.sector.api.sector;

import lombok.ToString;
import me.ponktacology.clashmc.api.Region;

@ToString(callSuper = true)
public class SectorRegion extends Region {

  public SectorRegion(int minX, int maxX, int minZ, int maxZ) {
    super(minX, maxX, Integer.MIN_VALUE, Integer.MAX_VALUE, minZ, maxZ);
  }
}

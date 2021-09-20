package me.ponktacology.clashmc.farmer.farmer.filler;

import me.ponktacology.clashmc.farmer.farmer.filler.pillar.Pillar;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class PillarFarmerFiller implements Filler {

  private final Pillar pillar;
  private final int maxY;
  private final Material material;

  public PillarFarmerFiller(Pillar pillar, int maxY, Material material) {
    this.maxY = maxY;
    this.pillar = pillar;
    this.material = material;
  }

  @Override
  public void fill() {
    this.pillar.fill(Bukkit.getWorld("world"), this.material, 4, this.maxY);
  }

  @Override
  public void completeNow() {
    this.fill();
  }
}

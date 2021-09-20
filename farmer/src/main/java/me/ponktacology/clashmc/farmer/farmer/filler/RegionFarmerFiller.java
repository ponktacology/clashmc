package me.ponktacology.clashmc.farmer.farmer.filler;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.farmer.FarmerPlugin;
import me.ponktacology.clashmc.farmer.farmer.filler.pillar.Pillar;
import me.ponktacology.clashmc.farmer.farmer.filler.region.FarmerRegion;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class RegionFarmerFiller implements Filler {

  private final FarmerRegion region;
  private final Material material;

  @Override
  public void fill() {
    int area = this.region.area();

    AtomicInteger index = new AtomicInteger();
    new BukkitRunnable() {
      @Override
      public void run() {

        if (area <= index.get()) {
          cancel();
          return;
        }

        Pillar pillar = region.pillar(index.getAndIncrement());
        new PillarFarmerFiller(pillar, region.getMaxY() + 1, material).fill();
      }
    }.runTaskTimer(FarmerPlugin.INSTANCE.getPlugin(), 0L, 20L);
  }

  @Override
  public void completeNow() {
    for (Pillar pillar : this.region.pillars()) {
      new PillarFarmerFiller(pillar, region.getMaxY() + 1, material).fill();
    }
  }
}

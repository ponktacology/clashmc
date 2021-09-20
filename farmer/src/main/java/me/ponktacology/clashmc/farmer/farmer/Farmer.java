package me.ponktacology.clashmc.farmer.farmer;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.farmer.farmer.filler.Filler;
import org.bukkit.Material;

@RequiredArgsConstructor
public class Farmer {

  protected final Material material;
  private final Filler filler;

  public void start() {
    this.filler.fill();
  }

  public void forceFinish() {
    this.filler.completeNow();
  }
}

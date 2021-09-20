package me.ponktacology.clashmc.farmer.farmer.type;

import me.ponktacology.clashmc.farmer.farmer.Farmer;
import me.ponktacology.clashmc.farmer.farmer.filler.PillarFarmerFiller;
import me.ponktacology.clashmc.farmer.farmer.filler.pillar.Pillar;
import org.bukkit.Location;
import org.bukkit.Material;

public class PillarFarmer extends Farmer {
  public PillarFarmer(Location location, Material material) {
    super(material, new PillarFarmerFiller(Pillar.of(location), location.getBlockY(), material));
  }
}

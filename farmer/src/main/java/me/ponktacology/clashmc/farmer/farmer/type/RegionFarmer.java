package me.ponktacology.clashmc.farmer.farmer.type;

import me.ponktacology.clashmc.farmer.farmer.Farmer;
import me.ponktacology.clashmc.farmer.farmer.filler.RegionFarmerFiller;
import me.ponktacology.clashmc.farmer.farmer.filler.region.FarmerRegion;
import org.bukkit.Material;

public class RegionFarmer extends Farmer {
  public RegionFarmer(FarmerRegion region, Material material) {
    super(material, new RegionFarmerFiller(region, material));
  }
}

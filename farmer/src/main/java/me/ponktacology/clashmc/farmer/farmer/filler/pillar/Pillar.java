package me.ponktacology.clashmc.farmer.farmer.filler.pillar;

import lombok.Data;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

@Data
@ToString
public class Pillar {

  private final int x, z;

  public static Pillar of(Location location) {
    return new Pillar(location.getBlockX(), location.getBlockZ());
  }

  public void fill(World world, Material material, int startY, int stopY) {
    for (int i = startY; i < stopY; i++) {
      Block block = world.getBlockAt(this.x, i, this.z);

      if (block.getType() == material
          || block.getType() == Material.CHEST
          || block.getType() == Material.TRAPPED_CHEST
          || block.getType() == Material.ENDER_CHEST) continue;

      block.setType(material, false);
    }
  }
}

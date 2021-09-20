package me.ponktacology.clashmc.sector.util;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class LocationUtil {
  private static final Set<Material> UNSAFE_MATERIALS = Sets.newHashSet();
  public static final int RADIUS = 3;
  public static final Vector3D[] VOLUME;

  static {
    UNSAFE_MATERIALS.add(Material.LAVA);
    UNSAFE_MATERIALS.add(Material.STATIONARY_LAVA);
    UNSAFE_MATERIALS.add(Material.FIRE);
    final List<Vector3D> pos = new ArrayList<>();
    for (int x = -3; x <= 3; ++x) {
      for (int y = -3; y <= 3; ++y) {
        for (int z = -3; z <= 3; ++z) {
          pos.add(new Vector3D(x, y, z));
        }
      }
    }
    Collections.sort(pos, Comparator.comparingInt(a -> a.x * a.x + a.y * a.y + a.z * a.z));
    VOLUME = pos.toArray(new Vector3D[0]);
  }

  public static class Vector3D {
    public int x;
    public int y;
    public int z;

    public Vector3D(final int x, final int y, final int z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }

  public static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
    return y > world.getMaxHeight() || !world.getBlockAt(x, y - 1, z).getType().isSolid();
  }

  public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) {
    final Block block = world.getBlockAt(x, y, z);
    final Block below = world.getBlockAt(x, y - 1, z);
    final Block above = world.getBlockAt(x, y + 1, z);
    return UNSAFE_MATERIALS.contains(below.getType())
        || block.getType().isSolid()
        || above.getType().isSolid()
        || isBlockAboveAir(world, x, y, z);
  }

  public static Location safeizeLocation(final Location location) {
    final World world = location.getWorld();
    int x = location.getBlockX();
    int y = (int) location.getY();
    int z = location.getBlockZ();
    final int origX = x;
    final int origY = y;
    final int origZ = z;
    location.setY(location.getWorld().getHighestBlockYAt(location));
    while (isBlockAboveAir(world, x, y, z)) {
      if (--y < 0) {
        y = origY;
        break;
      }
    }
    if (isBlockUnsafe(world, x, y, z)) {
      x = ((Math.round(location.getX()) == origX) ? (x - 1) : (x + 1));
      z = ((Math.round(location.getZ()) == origZ) ? (z - 1) : (z + 1));
    }
    for (int i = 0;
        isBlockUnsafe(world, x, y, z);
        x = origX + VOLUME[i].x, y = origY + VOLUME[i].y, z = origZ + VOLUME[i].z) {
      if (++i >= VOLUME.length) {
        x = origX;
        y = origY + 3;
        z = origZ;
        break;
      }
    }
    while (isBlockUnsafe(world, x, y, z)) {
      if (++y >= world.getMaxHeight()) {
        ++x;
        break;
      }
    }
    while (isBlockUnsafe(world, x, y, z)) {
      if (--y <= 1) {
        ++x;
        y = world.getHighestBlockYAt(x, z);
        if (x - 48 > location.getBlockX()) {
          return null;
        }
        continue;
      }
    }
    return new Location(world, x + 0.5, y, z + 0.5, location.getYaw(), location.getPitch());
  }
}

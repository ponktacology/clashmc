package me.ponktacology.clashmc.sector.sector.util;

import lombok.experimental.UtilityClass;
import me.ponktacology.clashmc.api.Region;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import net.jafama.FastMath;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@UtilityClass
public class RegionUtil {

  private static final int MAX_BAR_SHOW_DISTANCE = 30;

  public static Collection<Chunk> getChunksAroundPlayer(Player player) {
    int[] offset = {-1, 0, 1};

    World world = player.getWorld();
    int baseX = player.getLocation().getChunk().getX();
    int baseZ = player.getLocation().getChunk().getZ();

    Collection<Chunk> chunksAroundPlayer = new HashSet<>();
    for (int x : offset) {
      for (int z : offset) {
        world.getChunkAtAsync(baseX + x, baseZ + z, chunksAroundPlayer::add);
      }
    }

    return chunksAroundPlayer;
  }

  public static Sector getNearest(
      SectorCache sectorCache, Sector current, Location location, int offset) {
    double dist = distanceToBorder(current, location) + offset;

    World world = location.getWorld();
    int x = location.getBlockX();
    int y = location.getBlockY();
    int z = location.getBlockZ();

    Optional[] sectors = new Optional[4];

    sectors[0] = getSectorIn(sectorCache, new Location(world, x + dist, y, z));
    sectors[1] = getSectorIn(sectorCache, new Location(world, x - dist, y, z));
    sectors[2] = getSectorIn(sectorCache, new Location(world, x, y, z + dist));
    sectors[3] = getSectorIn(sectorCache, new Location(world, x, y, z - dist));

    for (Optional sectorOptional : sectors) {
      if (!sectorOptional.isPresent()) continue;

      Sector sector = (Sector) sectorOptional.get();

      if (sector.getName().equalsIgnoreCase(current.getName())) {
        continue;
      }

      if (sector.isSpawn() && current.isSpawn()) {
        continue;
      }

      return sector;
    }

    return null;
  }

  public static double distanceToBorder(Sector current, Location location) {
    Region region = current.getRegion();
    return Math.min(
            Math.min(
                Math.abs(region.getMinX() - location.getX()),
                Math.abs(region.getMaxX() - location.getX())),
            Math.min(
                Math.abs(region.getMinZ() - location.getZ()),
                Math.abs(region.getMaxZ() - location.getZ())))
        + 1.0;
  }

  public static double distanceToLocation(Location location1, Location location2) {
    Location location3 = location1.clone();
    Location location4 = location2.clone();

    location3.setY(0);
    location4.setY(0);

    return FastMath.sqrtQuick(location3.distanceSquared(location4));
  }

  public static float progress(double distance) {
    return (float) (1 - (distance / MAX_BAR_SHOW_DISTANCE));
  }

  public static Location getCenter(int y, Sector sector) {
    Region region = sector.getRegion();

    int centerX = (region.getMinX() + region.getMaxX()) / 2;
    int centerZ = (region.getMinZ() + region.getMaxZ()) / 2;

    return new Location(getWorld(sector), centerX, y, centerZ);
  }

  public static Optional<Sector> getSectorIn(Location location) {
    return getSectorIn(SectorPlugin.INSTANCE.getSectorCache(), location);
  }

  public static Optional<Sector> getSectorIn(SectorCache sectorCache, Location location) {
    for (Sector sector : sectorCache.values()) {
      if (sector.isSpecial() || !isInIgnoreY(location, sector)) continue;

      if (sector.isSpawn()) {
        return sectorCache.getLeastCrowded(SectorType.SPAWN);
      }

      return Optional.of(sector);
    }

    return Optional.empty();
  }

  public static boolean isIn(Location location, Region region) {
    return isInIgnoreY(location, region)
        && location.getY() >= region.getMinY()
        && location.getY() <= region.getMaxY();
  }

  public static boolean isInIgnoreY(Location location, Region region) {
    return location.getX() <= region.getMaxX()
        && location.getX() >= region.getMinX()
        && location.getZ() <= region.getMaxZ()
        && location.getZ() >= region.getMinZ();
  }

  public static boolean areOverlapping(Region region1, Region region2) {

    // To check if either rectangle is actually a line
    // For example : l1 ={-1,0} r1={1,1} l2={0,-1} r2={0,1}

    //l1 - min region1
    //l1 - region1.getMinX(), region1.getMinZ();
    //r1 - region1.getMaxX(), region1.getMaxZ()
    //l2 - region2.getMinX(), region2.getMinZ();
    //r2 - region2.getMaxX(), region2.getMaxZ()

    int x1 = region1.getMinX();
    int y1 = region1.getMinY();
    int x2 = region1.getMaxX();
    int y2 = region1.getMaxZ();

    int x3 = region2.getMinX();
    int y3 = region2.getMinY();
    int x4 = region2.getMaxX();
    int y4 = region2.getMaxZ();

    return (x1 < x4) && (x3 < x2) && (y1 < y4) && (y3 < y2);
  }

  public static boolean isInIgnoreY(Location location, Sector sector) {
    if (location == null || sector == null || sector.isSpecial()) return false;

    return isInIgnoreY(location, sector.getRegion());
  }

  public static boolean isIn(Location location, Sector sector) {
    if (location == null || sector == null || sector.isSpecial()) return false;

    return isIn(location, sector.getRegion());
  }

  public static World getWorld(Sector sector) {
    World world = null;
    switch (sector.getType()) {
      case SPAWN:
      case DEFAULT:
      case EVENT:
        world = Bukkit.getWorld("world");
        break;
      case END:
        world = Bukkit.getWorld("world_the_end");
        break;
      case NETHER:
        world = Bukkit.getWorld("world_nether");
        break;
    }

    return world;
  }

  public static Location getRandomLocationInSector(Sector sector) {
    Region region = sector.getRegion();
    World world = getWorld(sector);

    int x, y, z;
    boolean found = false;
    do {
      x = MathUtil.random(region.getMinX(), region.getMaxX());
      z = MathUtil.random(region.getMinZ(), region.getMaxZ());
      y = world.getHighestBlockYAt(x, z) + 1;

      if (y >= 60 && y <= 70) {
        found = true;
      } else continue;

    } while (!found);

    return new Location(world, x, y, z);
  }

  private static int determineSafeY(World world, int x, int z) {
    return world.getHighestBlockYAt(x, z);
  }

  public static void knock(Sector current, Player player) {
    Location center = getCenter(player.getLocation().getBlockY(), current);
    Location l = player.getLocation().subtract(center);
    double distance = player.getLocation().distance(center);
    Vector v = l.toVector().add(new Vector(0, 5, 0)).multiply(1.25 / distance);
    player.setVelocity(v.multiply(-1.5));
    player.playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 10);
  }
}

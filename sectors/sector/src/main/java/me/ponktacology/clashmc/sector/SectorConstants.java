package me.ponktacology.clashmc.sector;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SectorConstants {

  public static final int MAX_DISTANCE_SHOW_BORDER = 30;
  public static final int MIN_DISTANCE_ALLOW_BUILD = 15;
  public static final int MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD = 100;
  public static final long SECTOR_TRANSFER_DELAY_TIME = 10000L;
  public static final int BORDER = 3000;
  public static final String BYPASS_TELEPORT_COOLDOWN_PERMISSION = "bypass.teleport_cooldown";
  public static final String TPA_PERMISSION = "tpa";
  public static final String TPPOS_PERMISSION = "tppos";

  public static Location getSpawnLocation() {
    return new Location(Bukkit.getWorld("world_the_end"), 0.0, 1000.0, 0.0, 0F, 0F);
  }
}

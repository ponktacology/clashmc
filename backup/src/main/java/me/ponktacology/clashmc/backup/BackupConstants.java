package me.ponktacology.clashmc.backup;

import java.util.concurrent.TimeUnit;

public class BackupConstants {

  public static final long PLAYER_BACKUP_EVICTION_TIME =
      TimeUnit.MILLISECONDS.convert(2, TimeUnit.DAYS);

  public static final long WORLD_BACKUP_EVICTION_TIME =
      TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
}

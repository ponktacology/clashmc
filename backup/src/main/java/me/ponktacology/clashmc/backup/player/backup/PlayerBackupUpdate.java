package me.ponktacology.clashmc.backup.player.backup;

import lombok.Data;
import me.ponktacology.clashmc.backup.player.BackupPlayer;


@Data
public class PlayerBackupUpdate {

  
  private final BackupPlayer backupPlayer;
  
  private final PlayerBackup backup;

  private final boolean restoreInventory;
  private final boolean restoreEnder;
  private final boolean restoreRank;
}

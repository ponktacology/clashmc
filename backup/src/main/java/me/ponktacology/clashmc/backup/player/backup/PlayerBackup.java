package me.ponktacology.clashmc.backup.player.backup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.backup.BackupConstants;
import me.ponktacology.clashmc.backup.backup.Backup;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.api.sector.Sector;


@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class PlayerBackup extends Backup {

  private final Inventory inventory;
  private final int rankChange;
  private final int ping;
  private final double[] tps;
  private final String sector;

  private boolean inventoryRestored;
  private boolean enderRestored;
  private boolean rankRestored;

  public PlayerBackup(Inventory inventory, int rankChange, int ping, double[] tps,  Sector sector) {
    this.inventory = inventory;
    this.rankChange = rankChange;
    this.ping = ping;
    this.tps = tps;
    this.sector = sector.getName();
  }

  @Override
  public boolean hasExpired() {
    return System.currentTimeMillis() - this.getTimeStamp()
        > BackupConstants.PLAYER_BACKUP_EVICTION_TIME;
  }
}

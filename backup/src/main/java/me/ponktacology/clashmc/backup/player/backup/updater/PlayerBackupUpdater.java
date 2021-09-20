package me.ponktacology.clashmc.backup.player.backup.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackupUpdate;
import me.ponktacology.clashmc.backup.player.backup.updater.packet.PacketPlayerBackupUpdate;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;
import me.ponktacology.clashmc.sector.player.inventory.PlayerInventoryUpdate;
import me.ponktacology.clashmc.sector.player.inventory.updater.InventoryUpdater;
import org.bukkit.Bukkit;


import java.util.Optional;

@RequiredArgsConstructor
public class PlayerBackupUpdater implements Updater {

  
  private final NetworkService networkService;
  
  private final CorePlayerCache playerCache;
  
  private final GuildPlayerCache guildPlayerCache;
  
  private final InventoryUpdater inventoryUpdater;

  
  public void update( PlayerBackupUpdate update) {
    BackupPlayer backupPlayer = update.getBackupPlayer();
    PlayerBackup backup = update.getBackup();

    backupPlayer.save();

    if (update.isRestoreEnder() || update.isRestoreInventory()) {
      this.inventoryUpdater.update(
          new PlayerInventoryUpdate(
              backupPlayer,
              new InventoryUpdate(
                  backup.getInventory(), update.isRestoreInventory(), update.isRestoreEnder())));
    }

    if (update.isRestoreRank()) {
      if (!this.playerCache.isOnlineNotInAuthOrLobby(backupPlayer)) {
        this.guildPlayerCache
            .get(backupPlayer.getUuid())
            .ifPresent(
                it -> {
                  it.increaseRank(backup.getRankChange());
                  it.save();
                });
        return;
      }

      this.networkService.publish(
          new PacketPlayerBackupUpdate(backupPlayer.getUuid(), backup, true));
    }
  }

  @PacketHandler
  public void onPacket( PacketPlayerBackupUpdate packet) {
    Optional.ofNullable(Bukkit.getPlayer(packet.getPlayer()))
        .ifPresent(
            it -> {
              if (packet.isRestoreRank()) {
                PlayerBackup backup = packet.getBackup();

                this.guildPlayerCache
                    .get(it)
                    .ifPresent(
                        guildPlayer -> {
                          guildPlayer.increaseRank(backup.getRankChange());
                          guildPlayer.save();
                        });
              }
            });
  }
}

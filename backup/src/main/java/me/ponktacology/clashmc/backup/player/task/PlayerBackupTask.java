package me.ponktacology.clashmc.backup.player.task;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.factory.BackupPlayerFactory;
import me.ponktacology.clashmc.backup.util.BackupUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class PlayerBackupTask implements Runnable {


  private final BackupPlayerFactory playerFactory;

  @Override
  public void run() {
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      if (player == null || player.getHealth() == 0 || player.isDead()) continue;

      BackupPlayer backupPlayer =
          this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());

      backupPlayer.addBackup(BackupUtil.wrap(player));
      backupPlayer.save();
    }
  }
}

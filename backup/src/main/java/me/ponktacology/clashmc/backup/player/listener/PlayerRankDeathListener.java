package me.ponktacology.clashmc.backup.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.factory.BackupPlayerFactory;
import me.ponktacology.clashmc.backup.util.BackupUtil;
import me.ponktacology.clashmc.core.player.death.PlayerRankDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


@RequiredArgsConstructor
public class PlayerRankDeathListener implements Listener {


  private final BackupPlayerFactory playerFactory;

  private final TaskDispatcher taskDispatcher;

  @EventHandler
  public void onPlayerRankDeathEvent( PlayerRankDeathEvent event) {
    Player player = event.getPlayer();

    PlayerBackup backup = BackupUtil.wrap(player, event.getRankChange());

    this.taskDispatcher.runAsync(
        () -> {
          BackupPlayer backupPlayer =
              this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());

          backupPlayer.addBackup(backup);
          backupPlayer.save();
        });
  }
}

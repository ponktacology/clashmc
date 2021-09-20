package me.ponktacology.clashmc.backup.player.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.backup.BackupPlugin;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackupUpdate;
import me.ponktacology.clashmc.backup.player.backup.updater.PlayerBackupUpdater;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class PlayerBackupRankButton extends Button {

  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();
  private final PlayerBackupUpdater backupUpdater = BackupPlugin.INSTANCE.getBackupUpdater();


  private final BackupPlayer backupPlayer;

  private final PlayerBackup backup;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.GOLD_SWORD)
        .name("&eRanking")
        .lore(
            "",
            "&eStracony ranking: &f" + this.backup.getRankChange(),
            "&7Kliknij, aby przywrócić stracony ranking gracza.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      this.backup.setRankRestored(true);

      this.taskDispatcher.runAsync(
          () ->
              this.backupUpdater.update(
                  new PlayerBackupUpdate(this.backupPlayer, this.backup, false, false, true)));

      player.sendMessage(Text.colored("&aPomyślnie przywrócono ranking."));
    }
  }
}

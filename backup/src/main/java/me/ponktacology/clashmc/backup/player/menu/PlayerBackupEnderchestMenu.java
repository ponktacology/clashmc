package me.ponktacology.clashmc.backup.player.menu;

import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.backup.BackupPlugin;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackupUpdate;
import me.ponktacology.clashmc.backup.player.backup.updater.PlayerBackupUpdater;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.button.ConfirmationButton;
import me.ponktacology.clashmc.core.menu.button.DisplayButton;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.player.inventory.BukkitInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Map;

public class PlayerBackupEnderchestMenu extends SubMenu {

  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();
  private final PlayerBackupUpdater backupUpdater = BackupPlugin.INSTANCE.getBackupUpdater();

  private final BackupPlayer backupPlayer;
  private final PlayerBackup backup;

  public PlayerBackupEnderchestMenu(BackupPlayer backupPlayer, PlayerBackup backup) {
    super(36, false);
    this.backupPlayer = backupPlayer;
    this.backup = backup;
  }


  @Override
  public String getTitle(Player player) {
    return "&eEnderchest gracza &f" + this.backupPlayer.getName();
  }

  @Override
  public Map<Integer, Button> getButtons( Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);

    BukkitInventory inventory = new BukkitInventory(this.backup.getInventory());

    int index = 0;
    for (ItemStack itemStack : inventory.getEnderContentsItems()) {
      buttons.put(index++, new DisplayButton(itemStack, false));
    }

    buttons.put(
        35,
        new ConfirmationButton(
            true,
            (accepted) -> {
              if (accepted) {
                this.backup.setEnderRestored(true);

                this.taskDispatcher.runAsync(
                    () ->
                        this.backupUpdater.update(
                            new PlayerBackupUpdate(
                                this.backupPlayer, this.backup, false, true, false)));

                player.sendMessage(Text.colored("&aPomyślnie przywrócono enderchesta."));
              }
            },
            true));

    return buttons;
  }


  @Override
  public Menu getPrevious(Player player) {
    return new PlayerBackupManageMenu(this.backupPlayer, this.backup);
  }
}

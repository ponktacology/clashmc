package me.ponktacology.clashmc.backup.player.menu;

import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.menu.button.PlayerBackupEnderchestButton;
import me.ponktacology.clashmc.backup.player.menu.button.PlayerBackupInventoryButton;
import me.ponktacology.clashmc.backup.player.menu.button.PlayerBackupRankButton;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import org.bukkit.entity.Player;


import java.util.Map;

public class PlayerBackupManageMenu extends SubMenu {

  private final BackupPlayer backupPlayer;
  private final PlayerBackup backup;

  public PlayerBackupManageMenu(BackupPlayer backupPlayer, PlayerBackup backup) {
    super(18);
    this.backupPlayer = backupPlayer;
    this.backup = backup;
    this.setPlaceholder(true);
  }


  @Override
  public String getTitle(Player player) {
    return "&e" + this.backup.getFormattedDate();
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);

    buttons.put(11, new PlayerBackupInventoryButton(this.backupPlayer, this.backup));
    buttons.put(13, new PlayerBackupEnderchestButton(this.backupPlayer, this.backup));
    buttons.put(15, new PlayerBackupRankButton(this.backupPlayer, this.backup));

    return buttons;
  }


  @Override
  public Menu getPrevious(Player player) {
    return new PlayerBackupMenu(this.backupPlayer);
  }
}

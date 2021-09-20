package me.ponktacology.clashmc.backup.player.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.menu.button.PlayerBackupButton;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import org.bukkit.entity.Player;


import java.util.Map;

@RequiredArgsConstructor
public class PlayerBackupMenu extends Menu {


  private final BackupPlayer backupPlayer;


  @Override
  public String getTitle(Player player) {
    return "&eBackupy gracza &f" + this.backupPlayer.getName();
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    for (PlayerBackup backup : this.backupPlayer.getBackups()) {
      buttons.put(buttons.size(), new PlayerBackupButton(this.backupPlayer, backup));
    }

    return buttons;
  }
}

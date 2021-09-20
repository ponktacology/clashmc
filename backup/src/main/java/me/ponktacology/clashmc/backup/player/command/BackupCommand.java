package me.ponktacology.clashmc.backup.player.command;

import me.ponktacology.clashmc.backup.BackupPermissions;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.menu.PlayerBackupMenu;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


public class BackupCommand {

  @Command(value = "backup", description = "Otwiera menu backupów", async = true)
  @Permission(BackupPermissions.BACKUP_PLAYER)
  public void execute( @Sender Player sender, @Name("player") BackupPlayer player) {
    new PlayerBackupMenu(player).openMenu(sender);
  }
}

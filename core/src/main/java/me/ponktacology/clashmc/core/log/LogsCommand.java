package me.ponktacology.clashmc.core.log;

import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.menu.CoreMenuFactory;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

public class LogsCommand {

  private final CoreMenuFactory menuFactory;

  public LogsCommand(CoreMenuFactory menuFactory) {
    this.menuFactory = menuFactory;
  }

  @Command(
      value = {"commandlogs"},
      description = "Pokazuje logi komend gracza",
      async = true)
  @Permission(CorePermissions.LOGS)
  public void commandLogs(@Sender Player sender, @Name("player") CorePlayer corePlayer) {
    this.menuFactory.getCommandLogsMenu(corePlayer).openMenu(sender);
  }

  @Command(
      value = {"chatlogs", "messagelogs"},
      description = "Pokazuje logi wiadomości gracza",
      async = true)
  @Permission(CorePermissions.LOGS)
  public void chatLogs(@Sender Player sender, @Name("player") CorePlayer corePlayer) {
    this.menuFactory.getChatLogsMenu(corePlayer).openMenu(sender);
  }
}

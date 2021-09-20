package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.CorePermissions;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


public class WorkbenchCommand {

  @Command(value = {"workbench", "crafting", "wb"})
  @Permission(CorePermissions.WORKBENCH)
  public void execute( @Sender Player sender) {
    sender.openWorkbench(null, true);
  }
}

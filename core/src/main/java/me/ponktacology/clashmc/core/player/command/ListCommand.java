package me.ponktacology.clashmc.core.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


@RequiredArgsConstructor
public class ListCommand {

  @Command(value = "list", description = "Pokazuje listę graczy na serwerze")
  @Permission(CorePermissions.DEV)
  public void list( @Sender CommandSender sender) {
    sender.sendMessage("Ten sektor: " + Bukkit.getServer().getOnlinePlayers().size());
  }
}

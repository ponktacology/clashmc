package me.ponktacology.clashmc.core.util;

import me.ponktacology.clashmc.api.util.Console;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandUtil {

  public static UUID getUuid(CommandSender sender) {
    UUID issuer;
    if (sender instanceof ConsoleCommandSender) {
      issuer = Console.UUID;
    } else {
      Player senderPlayer = (Player) sender;

      issuer = senderPlayer.getUniqueId();
    }

    return issuer;
  }
}

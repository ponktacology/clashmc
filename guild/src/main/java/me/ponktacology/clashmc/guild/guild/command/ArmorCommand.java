package me.ponktacology.clashmc.guild.guild.command;

import me.ponktacology.clashmc.guild.GuildPermissions;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorMenu;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

public class ArmorCommand {

  @Command(value = "armor")
  @Permission(GuildPermissions.ARMOR)
  public void execute(@Sender Player sender) {
    new ArmorMenu().openMenu(sender);
  }
}

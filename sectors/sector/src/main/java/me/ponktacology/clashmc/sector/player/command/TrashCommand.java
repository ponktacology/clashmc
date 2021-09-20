package me.ponktacology.clashmc.sector.player.command;

import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class TrashCommand {

  @Command(value = "smietnik", description = "Otwiera śmietnik")
  public void trash( @Sender Player player) {
    player.openInventory(Bukkit.createInventory(null, 54, Text.colored("&eŚmietnik")));
  }
}

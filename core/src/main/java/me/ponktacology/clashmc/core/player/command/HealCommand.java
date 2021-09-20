package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.util.PlayerUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.entity.Player;


public class HealCommand {

  @Command(value = "heal", description = "Ulecza")
  @Permission(CorePermissions.FLY)
  public void execute( @Sender Player sender,  @Name("player") @Optional("self") Player player) {
    PlayerUtil.resetPlayer(player);

    sender.sendMessage(Text.colored("&aPomyślnie uleczono."));
  }
}

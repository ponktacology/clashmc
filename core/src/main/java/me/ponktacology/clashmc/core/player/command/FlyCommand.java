package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.entity.Player;


public class FlyCommand {

  @Command(value = "fly", description = "Włącza/Wyłącza fly")
  @Permission(CorePermissions.FLY)
  public void execute( @Sender Player sender,  @Name("player") @Optional("self") Player player) {
    boolean allowFlight = !player.getAllowFlight();

    if (!allowFlight) player.setFlying(false);

    player.setAllowFlight(allowFlight);

    if (allowFlight) player.setFlying(true);

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(allowFlight) + " &afly."));
  }
}

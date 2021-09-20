package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.entity.Player;

public class SpeedCommand {

  @Command(value = "walkspeed", description = "Zmienia szybkość chodzenia")
  @Permission(CorePermissions.SPEED)
  public void walkSpeed(@Sender Player sender, @Optional("0.2") @Name("speed") double value) {
    sender.setWalkSpeed(Double.valueOf(value).floatValue());
    sender.sendMessage(Text.colored("&aPomyślnie ustawiono prędkość chodzenia."));
  }

  @Command(value = "flyspeed", description = "Zmienia szybkość latania")
  @Permission(CorePermissions.SPEED)
  public void flySpeed(@Sender Player sender, @Optional("0.2") @Name("speed") double value) {
    sender.setFlySpeed(Double.valueOf(value).floatValue());
    sender.sendMessage(Text.colored("&aPomyślnie ustawiono prędkość latania."));
  }
}

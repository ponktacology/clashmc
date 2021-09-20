package me.ponktacology.clashmc.sector.sector.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorPermissions;
import me.ponktacology.clashmc.sector.api.motd.MotdSettings;
import me.ponktacology.clashmc.sector.api.motd.cache.MotdSettingsCache;
import me.ponktacology.clashmc.sector.api.motd.updater.MotdUpdater;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.command.CommandSender;


@RequiredArgsConstructor
public class MotdCommand {

  
  private final MotdUpdater motdUpdater;
  
  private final MotdSettingsCache motdSettingsCache;

  @Command(value = "motd first", description = "Ustawia pierwszą linijkę motd", async = true)
  @Permission(SectorPermissions.MOTD)
  public void execute1( @Sender CommandSender sender, @Name("motd") @Combined String line) {
    setLine(true, line);
    sender.sendMessage(Text.colored("&aPomyślnie zmieniono pierwszą linijkę motd."));
  }

  @Command(value = "motd second", description = "Ustawia drugą linijkę motd", async = true)
  @Permission(SectorPermissions.MOTD)
  public void execute2( @Sender CommandSender sender, @Name("motd") @Combined String line) {
    setLine(false, line);
    sender.sendMessage(Text.colored("&aPomyślnie zmieniono drugą linijkę motd."));
  }

  private void setLine(boolean first, String line) {
    MotdSettings motdSettings = motdSettingsCache.get();
    if (first) {
      motdSettings.setFirst(line);
    } else {
      motdSettings.setSecond(line);
    }

    motdUpdater.update(motdSettings);
  }
}

package me.ponktacology.clashmc.sector.sector.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.SectorPermissions;
import me.ponktacology.clashmc.sector.sector.menu.SectorMenuFactory;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class SectorCommand {


  private final SectorMenuFactory menuFactory;

  @Command(
      value = {"sectors", "sektory"},
      description = "Pokazuję liste sektorów")
  @Permission(SectorPermissions.SECTORS)
  public void execute( @Sender Player sender) {
    menuFactory.getSectorMenu().openMenu(sender);
  }
}

package me.ponktacology.clashmc.sector.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.sector.SectorPermissions;
import me.ponktacology.clashmc.sector.player.inventory.menu.InventoryMenu;
import me.ponktacology.clashmc.sector.player.inventory.updater.InventoryUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Optional;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class InvSeeCommand {


  private final InventoryUpdater inventoryUpdater;

  @Command(
      value = {"invsee", "openinv"},
      description = "Otwiera ekwipunek gracza")
  @Permission(SectorPermissions.INV_SEE)
  public void execute( @Sender Player sender, @Optional("self") CorePlayer corePlayer) {
    this.inventoryUpdater.request(
        corePlayer,
        (inventory) -> new InventoryMenu(corePlayer, inventory, false).openMenu(sender));
  }
}

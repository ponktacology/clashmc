package me.ponktacology.clashmc.sector.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorPermissions;
import me.ponktacology.clashmc.sector.player.inventory.menu.InventoryMenu;
import me.ponktacology.clashmc.sector.player.inventory.updater.InventoryUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Optional;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class EnderchestCommand {


  private final InventoryUpdater inventoryUpdater;

  @Command(
      value = {"enderchest", "ec"},
      description = "Otwiera enderchesta gracza")
  @Permission(CorePermissions.ENDERCHEST)
  public void execute( @Sender Player sender,  @Optional("self") CorePlayer corePlayer) {
    boolean self = corePlayer.getUuid().equals(sender.getUniqueId());

    if (!self && !sender.hasPermission(SectorPermissions.ENDERCHEST_OTHERS)) {
      sender.sendMessage(
          Text.colored("&cNie znaleziono takiej komendy. Wszystkie komendy pod &7/pomoc&c."));
      return;
    }

    if (self) {
      sender.openInventory(sender.getEnderChest());
      return;
    }

    this.inventoryUpdater.request(
        corePlayer, (inventory) -> new InventoryMenu(corePlayer, inventory, true).openMenu(sender));
  }
}

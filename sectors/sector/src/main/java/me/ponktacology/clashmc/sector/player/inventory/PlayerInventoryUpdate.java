package me.ponktacology.clashmc.sector.player.inventory;

import lombok.Data;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;


@Data
public class PlayerInventoryUpdate {

  private final BukkitPlayerWrapper player;

  private final InventoryUpdate update;
}

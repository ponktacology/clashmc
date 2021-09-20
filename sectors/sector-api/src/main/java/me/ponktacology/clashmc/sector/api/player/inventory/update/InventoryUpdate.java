package me.ponktacology.clashmc.sector.api.player.inventory.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class InventoryUpdate {


  private final Inventory inventory;
  private boolean updateInventory;
  private boolean updateEnderchest;
}

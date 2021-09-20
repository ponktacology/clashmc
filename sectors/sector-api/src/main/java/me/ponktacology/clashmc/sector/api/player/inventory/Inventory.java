package me.ponktacology.clashmc.sector.api.player.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Inventory {
  private static final Inventory EMPTY_INVENTORY =
      new Inventory(new byte[] {0}, new byte[] {0}, new byte[] {0});

  private byte[] inventoryContents;
  private byte[] armorContents;
  private byte[] enderContents;

  public boolean isEmpty() {
    return this.equals(EMPTY_INVENTORY);
  }

  public static Inventory emptyInventory() {
    return EMPTY_INVENTORY;
  }
}

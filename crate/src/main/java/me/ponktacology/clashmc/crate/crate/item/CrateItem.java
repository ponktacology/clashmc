package me.ponktacology.clashmc.crate.crate.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
@Setter
public class CrateItem {

  private final String name;
  private final ItemStack item;
  private String displayName;
  private int index;
  private double chance;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CrateItem that = (CrateItem) o;

    return this.getName().equals(that.getName());
  }
}

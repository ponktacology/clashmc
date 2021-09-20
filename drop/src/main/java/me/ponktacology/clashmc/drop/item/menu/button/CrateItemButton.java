package me.ponktacology.clashmc.drop.item.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.crate.crate.item.CrateItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class CrateItemButton extends Button {

  private final CrateItem crateItem;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(crateItem.getItem().clone())
        .name(crateItem.getDisplayName())
        .build();
  }
}

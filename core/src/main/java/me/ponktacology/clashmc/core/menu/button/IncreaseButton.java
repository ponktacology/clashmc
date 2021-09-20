package me.ponktacology.clashmc.core.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.Callback;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class IncreaseButton extends Button {

  
  private final Number increment, value, maxValue;

  
  private final Callback<Number> onValueChange;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.SKULL_ITEM)
        .skull("MHF_ArrowUp")
        .name("&eZwiększ")
        .lore( "&7Kliknij, aby zwiększyć o " + increment.toString() + ".")
        .build();
  }

  @Override
  public boolean shouldUpdate(Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      double sum = increment.doubleValue() + value.doubleValue();
      double max = maxValue.doubleValue();

      onValueChange.accept(Math.min(sum, max));
      return false;
    }

    return super.shouldUpdate(player, clickType);
  }
}

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
public class DecreaseButton extends Button {

  
  private final Number increment, value, minValue;

  
  private final Callback<Number> onValueChange;


  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.SKULL_ITEM)
        .skull("MHF_ArrowDown")
        .name("&eZmniejsz")
        .lore( "&7Kliknij, aby zmniejszyć o " + increment.toString() + ".")
        .build();
  }

  @Override
  public boolean shouldUpdate(Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      double sum = value.doubleValue() - increment.doubleValue();
      double min = minValue.doubleValue();

      onValueChange.accept(Math.max(sum, min));
      return false;
    }

    return super.shouldUpdate(player, clickType);
  }
}

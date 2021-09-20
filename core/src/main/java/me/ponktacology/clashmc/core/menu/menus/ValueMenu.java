package me.ponktacology.clashmc.core.menu.menus;

import me.ponktacology.clashmc.api.Callback;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.button.DecreaseButton;
import me.ponktacology.clashmc.core.menu.button.IncreaseButton;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.HashMap;
import java.util.Map;

public class ValueMenu extends Menu {

  private final String title;
  private final Number value, minValue, maxValue, valueChange1, valueChange2, valueChange3;
  private final Callback<Number> onValueSet;

  public ValueMenu(
      String title,
      Number value,
      Number minValue,
      Number maxValue,
      Number valueChange1,
      Number valueChange2,
      Number valueChange3,
      Callback<Number> onValueSet) {
    this.title = title;
    this.value = value;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.valueChange1 = valueChange1;
    this.valueChange2 = valueChange2;
    this.valueChange3 = valueChange3;
    this.onValueSet = onValueSet;
    setPlaceholder(true);
  }

  @Override
  public String getTitle(Player player) {
    return title;
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    Button valueButton =
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name("&eWartość: &f" + value.doubleValue())
                .lore( "&7Kliknij, aby zatwierdzić.")
                .build();
          }

          @Override
          public void clicked(Player player,  ClickType clickType) {
            if (clickType.isLeftClick()) {
              onValueSet.accept(value);
              return;
            }

            super.clicked(player, clickType);
          }
        };

    Callback<Number> valueChangeCallback =
        result -> {
          new ValueMenu(
                  title,
                  result,
                  minValue,
                  maxValue,
                  valueChange1,
                  valueChange2,
                  valueChange3,
                  onValueSet)
              .openMenu(player);
        };

    Button increaseValue1Button =
        new IncreaseButton(valueChange1, value, maxValue, valueChangeCallback);
    Button increaseValue2Button =
        new IncreaseButton(valueChange2, value, maxValue, valueChangeCallback);
    Button increaseValue3Button =
        new IncreaseButton(valueChange3, value, maxValue, valueChangeCallback);
    Button decreaseValue1Button =
        new DecreaseButton(valueChange1, value, minValue, valueChangeCallback);
    Button decreaseValue2Button =
        new DecreaseButton(valueChange2, value, minValue, valueChangeCallback);
    Button decreaseValue3Button =
        new DecreaseButton(valueChange3, value, minValue, valueChangeCallback);

    buttons.put(10, decreaseValue3Button);
    buttons.put(11, decreaseValue2Button);
    buttons.put(12, decreaseValue1Button);
    buttons.put(13, valueButton);
    buttons.put(14, increaseValue1Button);
    buttons.put(15, increaseValue2Button);
    buttons.put(16, increaseValue3Button);
    buttons.put(18, getPlaceholderButton());

    return buttons;
  }
}

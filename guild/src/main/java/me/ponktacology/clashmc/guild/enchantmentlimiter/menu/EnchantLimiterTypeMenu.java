package me.ponktacology.clashmc.guild.enchantmentlimiter.menu;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantType;
import me.ponktacology.clashmc.guild.enchantmentlimiter.menu.button.EnchantTypeButton;
import org.bukkit.entity.Player;

import java.util.Map;

public class EnchantLimiterTypeMenu extends Menu {
  @Override
  public String getTitle(Player player) {
    return "&eWybierz typ enchantu";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newConcurrentMap();

    for(EnchantType type : EnchantType.values()) {
        buttons.put(buttons.size(), new EnchantTypeButton(type));
    }

    return buttons;
  }
}

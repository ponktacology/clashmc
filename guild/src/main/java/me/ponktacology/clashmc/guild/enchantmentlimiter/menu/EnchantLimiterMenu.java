package me.ponktacology.clashmc.guild.enchantmentlimiter.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantType;
import me.ponktacology.clashmc.guild.enchantmentlimiter.cache.EnchantLimiterSettingsCache;
import me.ponktacology.clashmc.guild.enchantmentlimiter.menu.button.EnchantLimiterButton;
import me.ponktacology.clashmc.guild.enchantmentlimiter.updater.EnchantLimiterSettingsUpdater;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class EnchantLimiterMenu extends Menu {

  private final EnchantType type;
  private final EnchantLimiterSettingsCache enchantLimiterSettingsCache;
  private final EnchantLimiterSettingsUpdater enchantLimiterSettingsUpdater;
  private final TaskDispatcher taskDispatcher;

  @Override
  public String getTitle(Player player) {
    return "&eLimity enchantów";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    for (Enchantment enchantment : Enchantment.values()) {
      buttons.put(
          buttons.size(),
          new EnchantLimiterButton(
              this.type,
              enchantment,
              this.enchantLimiterSettingsCache,
              this.enchantLimiterSettingsUpdater,
              this.taskDispatcher,
              this));
    }

    return buttons;
  }
}

package me.ponktacology.clashmc.guild.enchantmentlimiter.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.menus.ValueSubMenu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantLimiterSettings;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantType;
import me.ponktacology.clashmc.guild.enchantmentlimiter.cache.EnchantLimiterSettingsCache;
import me.ponktacology.clashmc.guild.enchantmentlimiter.updater.EnchantLimiterSettingsUpdater;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class EnchantLimiterButton extends Button {

  private final EnchantType type;
  private final Enchantment enchantment;
  private final EnchantLimiterSettingsCache enchantLimiterSettingsCache;
  private final EnchantLimiterSettingsUpdater enchantLimiterSettingsUpdater;
  private final TaskDispatcher taskDispatcher;
  private final Menu previousMenu;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.PAPER)
        .name("&e" + this.enchantment.getName())
        .lore( "&eLimit: &f" + this.enchantLimiterSettingsCache.get().get(this.type, this.enchantment))
        .build();
  }

  @Override
  public void clicked(Player player, ClickType clickType) {
    if (clickType.isLeftClick()) {
      EnchantLimiterSettings limiterSettings = this.enchantLimiterSettingsCache.get();
      new ValueSubMenu(
              "&7Limit enchantu",
              limiterSettings.get(this.type, this.enchantment),
              0,
              enchantment.getMaxLevel(),
              1,
              1,
              1,
              value -> {
                taskDispatcher.runAsync(
                    () -> {
                      limiterSettings.set(this.type, this.enchantment, value.intValue());
                      this.enchantLimiterSettingsUpdater.update();
                    });
                this.previousMenu.openMenu(player);
                player.sendMessage(Text.colored("&aZatwierdzono limit enchantu."));
              },
              this.previousMenu)
          .openMenu(player);
    }
  }
}

package me.ponktacology.clashmc.guild.enchantmentlimiter.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class EnchantTypeButton extends Button {

  private final EnchantType type;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(this.type.getIcon())
        .name("&e".concat(this.type.getDisplayName()))
        .build();
  }

  @Override
  public void clicked(Player player, ClickType clickType) {
    if (!clickType.isLeftClick()) return;

    GuildPlugin.INSTANCE.getMenuFactory().getEnchantLimiterMenu(this.type).openMenu(player);
  }
}

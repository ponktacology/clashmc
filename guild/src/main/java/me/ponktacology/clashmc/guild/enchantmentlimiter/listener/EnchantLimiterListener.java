package me.ponktacology.clashmc.guild.enchantmentlimiter.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantType;
import me.ponktacology.clashmc.guild.enchantmentlimiter.cache.EnchantLimiterSettingsCache;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

@RequiredArgsConstructor
public class EnchantLimiterListener implements Listener {

  private final EnchantLimiterSettingsCache enchantLimiterSettingsCache;

  @EventHandler
  public void onEnchantItemtEvent(EnchantItemEvent event) {
    for (Enchantment enchantment : new HashSet<>(event.getEnchantsToAdd().keySet())) {
      int limit =
          this.enchantLimiterSettingsCache
              .get()
              .get(EnchantType.from(event.getItem()), enchantment);
      int level = event.getEnchantsToAdd().get(enchantment);

      if (level > limit) {
        if (limit == 0) {
          event.getEnchantsToAdd().remove(enchantment);
        } else {
          event.getEnchantsToAdd().remove(enchantment);
          event.getEnchantsToAdd().put(enchantment, limit);
        }
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClickEvent(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();
    Inventory inventory = event.getInventory();
    if (inventory.getType() == InventoryType.ANVIL && event.getRawSlot() == 2) {
      ItemStack result = inventory.getItem(2);
      if (result != null) {
        event.setCancelled(true);

        for (Enchantment enchantment : new HashSet<>(result.getEnchantments().keySet())) {
          int limit =
              this.enchantLimiterSettingsCache.get().get(EnchantType.from(result), enchantment);
          int level = result.getEnchantmentLevel(enchantment);

          if (level > limit) {

            if (limit == 0) {
              result.removeEnchantment(enchantment);
            } else {
              result.removeEnchantment(enchantment);
              result.addEnchantment(enchantment, limit);
            }

            player.updateInventory();
          }
        }

        inventory.setItem(0, new ItemStack(Material.AIR));
        inventory.setItem(1, new ItemStack(Material.AIR));
        inventory.setItem(2, new ItemStack(Material.AIR));

        if (event.getClick().name().contains("SHIFT")) {
          player.getInventory().addItem(result);
        } else {
          event.setCursor(result);
        }
      }
    }
  }
}

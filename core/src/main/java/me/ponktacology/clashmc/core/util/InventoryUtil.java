package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@UtilityClass
@Slf4j
public class InventoryUtil {

  public static boolean hasItems(Player player, List<ItemStack> items) {
    for (ItemStack item : items) {
      if (!hasItem(player, item)) return false;
    }

    return true;
  }

  public static int countItemsIgnoreItemMeta(Player player, ItemStack item) {
    if (item == null || item.getType() == Material.AIR) return 0;

    PlayerInventory inventory = player.getInventory();
    int count = 0;
    for (int i = 0; i < inventory.getSize(); i++) {
      ItemStack itemStack = inventory.getItem(i);

      if (itemStack == null || !isSimilarExceptItemMeta(item, itemStack)) continue;

      count += itemStack.getAmount();
    }

    return count;
  }

  private static boolean isSimilarExceptItemMeta(ItemStack stack1, ItemStack stack2) {
    if (stack1 == null || stack2 == null) return false;

    return stack1.getType() == stack2.getType() && stack1.getDurability() == stack2.getDurability();
  }

  public static int countItems(Player player, ItemStack item) {
    if (item == null || item.getType() == Material.AIR) return 0;

    PlayerInventory inventory = player.getInventory();
    int count = 0;
    for (int i = 0; i < inventory.getSize(); i++) {
      ItemStack itemStack = inventory.getItem(i);

      if (itemStack == null || !itemStack.isSimilar(item)) continue;

      count += itemStack.getAmount();
    }

    return count;
  }

  public static boolean hasItem(Player player, ItemStack item, int amount) {
    if (item == null || item.getType() == Material.AIR) return false;

    return player.getInventory().containsAtLeast(item, amount);
  }

  public static boolean hasItem(Player player, Material material, int amount) {
    return hasItem(player, new ItemStack(material, amount));
  }

  public static boolean hasItem(Player player, ItemStack item) {
    if (item == null || item.getType() == Material.AIR) return true;

    return hasItem(player, item, item.getAmount());
  }

  public static void addItem(Player player, ItemStack item) {
    if (item == null) return;

    Map<Integer, ItemStack> leftOver = player.getInventory().addItem(item.clone());

    for (ItemStack leftoverItem : leftOver.values()) {
      player.getWorld().dropItem(player.getLocation(), leftoverItem.clone());
    }
  }

  public static void addItem(Player player, List<ItemStack> items) {
    Map<Integer, ItemStack> leftOver =
        player
            .getInventory()
            .addItem(
                items.stream()
                    .filter(Objects::nonNull)
                    .map(ItemStack::clone)
                    .toArray(ItemStack[]::new));

    for (ItemStack leftoverItem : leftOver.values()) {
      player.getWorld().dropItem(player.getLocation(), leftoverItem.clone());
    }
  }

  public static void removeItemIgnoreItemMeta(Player player, ItemStack item) {
    if (item == null || item.getType() == Material.AIR) return;

    int amountLeft = item.getAmount();
    ItemStack[] contents = player.getInventory().getContents();
    for (int i = 0; i < contents.length; i++) {
      ItemStack itemStack = contents[i];
      if (itemStack != null
          && itemStack.getType() == item.getType()
          && itemStack.getDurability() == item.getDurability()) {
        if (amountLeft >= itemStack.getAmount()) {
          amountLeft -= itemStack.getAmount();

          player.getInventory().setItem(i, new ItemStack(Material.AIR));
        } else {
          itemStack.setAmount(itemStack.getAmount() - amountLeft);
        }

        if (amountLeft == 0) return;
      }
    }
  }

  public static void removeItem(Player player, Material material, int amount) {
    removeItem(player, new ItemStack(material, amount));
  }

  public static void removeItem(Player player, ItemStack item) {
    if (item == null || item.getType() == Material.AIR) return;

    player.getInventory().removeItem(item);
  }

  public static void removeItems(Player player, List<ItemStack> items) {
    player
        .getInventory()
        .removeItem(items.stream().filter(Objects::nonNull).toArray(ItemStack[]::new));
  }
}

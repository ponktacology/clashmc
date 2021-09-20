package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;

@UtilityClass
public class ItemUtil {

  public static int getFortuneLevel(ItemStack item) {
    return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
  }

  public static void tryToTakeDurability18(Player player) {
    ItemStack itemInMainHand = player.getItemInHand();

    if (itemInMainHand == null || itemInMainHand.getType() == Material.AIR) {
      return;
    }

    String tool = itemInMainHand.getType().name().toUpperCase(Locale.ROOT);

    if (!(tool.endsWith("_PICKAXE")
        || tool.endsWith("_AXE")
        || tool.endsWith("_SWORD")
        || tool.endsWith("_SHOVEL"))) return;

    if (itemInMainHand.getType().isBlock()) {
      return;
    }

    short currentDamage = itemInMainHand.getDurability();
    short maxDamage = itemInMainHand.getType().getMaxDurability();

    short newDamage = (short) (currentDamage + 1);

    if (newDamage >= maxDamage) {
      player.setItemInHand(new ItemStack(Material.AIR));
      player.updateInventory();
    } else itemInMainHand.setDurability(newDamage);
  }

  public boolean isSimilar(ItemStack itemStack, ItemStack itemStack2) {
    if (itemStack.isSimilar(itemStack2)) return true;

    if (itemStack.getType() != itemStack2.getType()) return false;
    if (itemStack.getDurability() != itemStack2.getDurability()) return false;
    if (itemStack.hasItemMeta() != itemStack2.hasItemMeta()) return false;

    ItemMeta itemMeta = itemStack.getItemMeta();
    ItemMeta itemMeta2 = itemStack2.getItemMeta();

    if (itemMeta.hasDisplayName() != itemMeta2.hasDisplayName()) return false;

    if (itemMeta.hasDisplayName()) {
      if (!itemMeta.getDisplayName().equals(itemMeta2.getDisplayName())) return false;
    }

    if (itemMeta.hasLore() != itemMeta2.hasLore()) return false;

    if (itemMeta.hasLore()) {
      return itemMeta.getLore().equals(itemMeta2.getLore());
    }

    return true;
  }

  public static Optional<Recipe> getRecipe(Material material) {
    return Bukkit.getServer().getRecipesFor(new ItemStack(material)).stream().findFirst();
  }

  public static void removeRecipe(Material m) {
    Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
    Recipe recipe;
    while (it.hasNext()) {
      recipe = it.next();
      if (recipe != null && recipe.getResult().getType() == m) {
        it.remove();
      }
    }
  }
}

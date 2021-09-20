package me.ponktacology.clashmc.guild.recipe;

import lombok.Getter;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

@Getter
public abstract class CustomRecipe extends ShapedRecipe {

  private final String name;

  public CustomRecipe(ItemStack result, String name) {
    super(result);
    this.name = name;
  }



  public ItemStack[] getItems() {
    ItemStack[] itemStacks = new ItemStack[9];
    String[] rows = this.getShape();
    int i = 0;
    for (int x = 0; x < 3; x++) {
      char[] ingredients = rows[x].toCharArray();

      for (int y = 0; y < 3; y++) {
        char ingredient = ingredients[y];

        ItemStack item = this.getIngredientMap().get(ingredient);

        itemStacks[i++] = item == null ? new ItemBuilder(Material.AIR).build() : item;
      }
    }
    return itemStacks;
  }
}

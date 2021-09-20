package me.ponktacology.clashmc.guild.recipe.armor;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.core.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

public class ArmorRecipeCache extends KeyValueCache<Material, Recipe> {

  {
    this.add(Material.DIAMOND_HELMET);
    this.add(Material.DIAMOND_CHESTPLATE);
    this.add(Material.DIAMOND_LEGGINGS);
    this.add(Material.DIAMOND_BOOTS);
    this.add(Material.DIAMOND_SWORD);
  }

  private void add(Material material) {
    Recipe recipe = ItemUtil.getRecipe(material).orElseThrow(NullPointerException::new);
    super.add(material, recipe);
  }
}

package me.ponktacology.clashmc.guild.recipe;

import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;

public class CrappleRecipe extends CustomRecipe {

  public CrappleRecipe() {
    super(new ItemBuilder(Material.GOLDEN_APPLE).build(), "Refil");
    this.shape(
            "SPS",
            "PAP",
            "SPS");
    this.setIngredient('A', Material.APPLE);
    this.setIngredient('P', Material.GOLD_INGOT);
  }
}

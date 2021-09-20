package me.ponktacology.clashmc.farmer.farmer.recipe;

import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.recipe.CustomRecipe;
import org.bukkit.Material;

public class ObsidianFarmerRecipe extends CustomRecipe {

  public ObsidianFarmerRecipe() {
    super(new ItemBuilder(Material.ENDER_PORTAL_FRAME)
            .name("&eBoyFarmer")
            .lore("&eGeneruje obsydian do 4 poziomu Y.")
            .build(), "BoyFarmer");
    this.shape("OOO", "OGO", "OOO");
    this.setIngredient('G', Material.PISTON_BASE);
    this.setIngredient('O', Material.OBSIDIAN);
  }
}

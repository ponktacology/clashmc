package me.ponktacology.clashmc.farmer.farmer.recipe;

import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.recipe.CustomRecipe;
import org.bukkit.Material;

public class SandFarmerRecipe extends CustomRecipe {

  public SandFarmerRecipe() {
    super(
        new ItemBuilder(Material.ENDER_PORTAL_FRAME)
            .name("&eSandFarmer")
            .lore("&eGeneruje piasek do 4 poziomu Y.")
            .build(),
        "SandFarmer");
    this.shape("SSS", "SPS", "SSS");
    this.setIngredient('P', Material.PISTON_BASE);
    this.setIngredient('S', Material.SAND);
  }
}

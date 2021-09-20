package me.ponktacology.clashmc.drop.generator.recipe;

import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class GeneratorRecipe extends CustomRecipe {

  public GeneratorRecipe() {
    super(
        new ItemBuilder(Material.STONE)
            .enchantment(Enchantment.DURABILITY, 10)
            .name("&7Generator")
            .lore(
                "",
                "&ePoziom: &f" + 0,
                "&7Kliknij prawym, aby ulepszyć.",
                "&eGenerator możesz zniszczyć złotym kilofem.")
            .build(),
        "Generator");

    this.shape("SSS", "SPS", "SSS");
    this.setIngredient('S', Material.STONE);
    this.setIngredient('P', Material.PISTON_BASE);
  }
}

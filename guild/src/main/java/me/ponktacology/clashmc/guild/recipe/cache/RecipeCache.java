package me.ponktacology.clashmc.guild.recipe.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.guild.recipe.CustomRecipe;
import org.bukkit.Bukkit;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeCache extends IgnoreCaseKeyValueCache<CustomRecipe> {

  public void add(CustomRecipe recipe) {
    super.add(recipe.getName(), recipe);
    Bukkit.getServer().addRecipe(recipe);
  }

  public Optional<CustomRecipe> remove(CustomRecipe recipe) {
    Bukkit.getRecipesFor(recipe.getResult()).remove(recipe);

    return super.remove(recipe.getName());
  }

  public Set<CustomRecipe> valuesSorted() {
    return super.values().stream()
        .sorted(Comparator.comparing(CustomRecipe::getName))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }
}

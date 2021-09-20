package me.ponktacology.clashmc.guild.recipe.armor;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.guild.GuildPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class ArmorMenu extends Menu {

  private final ArmorRecipeCache armorRecipeCache = GuildPlugin.INSTANCE.getArmorRecipeCache();

  @Override
  public String getTitle(Player player) {
    return "&eUstaw przedmioty";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    for (Material material :
        armorRecipeCache.keys().stream()
            .sorted(Enum::compareTo)
            .collect(Collectors.toCollection(LinkedHashSet::new))) {
      buttons.put(buttons.size(), new ArmorButton(material));
    }

    return buttons;
  }
}

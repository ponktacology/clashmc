package me.ponktacology.clashmc.guild.recipe.armor.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorSettings;
import me.ponktacology.clashmc.guild.recipe.armor.factory.ArmorSettingsFactory;

public class ArmorSettingsCache extends SettingsCache<ArmorSettings> {
  public ArmorSettingsCache(ArmorSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

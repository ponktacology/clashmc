package me.ponktacology.clashmc.guild.recipe.armor.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorSettings;

public class ArmorSettingsFactory extends SettingsFactory<ArmorSettings> {
  public ArmorSettingsFactory(DataService dataService) {
    super(dataService, ArmorSettings.class);
  }
}

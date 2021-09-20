package me.ponktacology.clashmc.guild.player.combattag.global.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.guild.player.combattag.global.CombatTagSettings;

public class CombatTagSettingsFactory extends SettingsFactory<CombatTagSettings> {
  public CombatTagSettingsFactory(DataService dataService) {
    super(dataService, CombatTagSettings.class);
  }
}

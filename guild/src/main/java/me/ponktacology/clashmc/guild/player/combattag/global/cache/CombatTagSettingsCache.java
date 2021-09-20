package me.ponktacology.clashmc.guild.player.combattag.global.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.guild.player.combattag.global.CombatTagSettings;
import me.ponktacology.clashmc.guild.player.combattag.global.factory.CombatTagSettingsFactory;

public class CombatTagSettingsCache extends SettingsCache<CombatTagSettings> {
  public CombatTagSettingsCache(CombatTagSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

package me.ponktacology.clashmc.sector.api.motd.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.sector.api.motd.MotdSettings;
import me.ponktacology.clashmc.sector.api.motd.factory.MotdSettingsFactory;

public class MotdSettingsCache extends SettingsCache<MotdSettings> {
  public MotdSettingsCache(MotdSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

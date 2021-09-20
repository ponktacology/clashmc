package me.ponktacology.clashmc.sector.api.whitelist.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.sector.api.whitelist.WhitelistSettings;
import me.ponktacology.clashmc.sector.api.whitelist.factory.WhitelistSettingsFactory;

public class WhitelistSettingsCache extends SettingsCache<WhitelistSettings> {

  public WhitelistSettingsCache(WhitelistSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

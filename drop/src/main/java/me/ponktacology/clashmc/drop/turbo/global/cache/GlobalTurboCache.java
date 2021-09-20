package me.ponktacology.clashmc.drop.turbo.global.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.drop.turbo.global.GlobalTurbo;
import me.ponktacology.clashmc.drop.turbo.global.factory.GlobalTurboFactory;

public class GlobalTurboCache extends SettingsCache<GlobalTurbo> {
  public GlobalTurboCache(GlobalTurboFactory settingsFactory) {
    super(settingsFactory);
  }
}

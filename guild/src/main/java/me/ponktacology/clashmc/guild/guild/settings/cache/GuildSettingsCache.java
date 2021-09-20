package me.ponktacology.clashmc.guild.guild.settings.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;
import me.ponktacology.clashmc.guild.guild.settings.factory.GuildSettingsFactory;

public class GuildSettingsCache extends SettingsCache<GuildSettings> {
  public GuildSettingsCache(GuildSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

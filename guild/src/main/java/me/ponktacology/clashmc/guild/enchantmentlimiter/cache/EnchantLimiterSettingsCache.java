package me.ponktacology.clashmc.guild.enchantmentlimiter.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantLimiterSettings;
import me.ponktacology.clashmc.guild.enchantmentlimiter.factory.EnchantLimiterSettingsFactory;

public class EnchantLimiterSettingsCache extends SettingsCache<EnchantLimiterSettings> {
  public EnchantLimiterSettingsCache(EnchantLimiterSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

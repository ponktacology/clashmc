package me.ponktacology.clashmc.guild.enchantmentlimiter.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantLimiterSettings;

public class EnchantLimiterSettingsFactory extends SettingsFactory<EnchantLimiterSettings> {
  public EnchantLimiterSettingsFactory(DataService dataService) {
    super(dataService, EnchantLimiterSettings.class);
  }
}

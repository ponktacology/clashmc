package me.ponktacology.clashmc.guild.enchantmentlimiter.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantLimiterSettings;
import me.ponktacology.clashmc.guild.enchantmentlimiter.cache.EnchantLimiterSettingsCache;
import me.ponktacology.clashmc.guild.enchantmentlimiter.factory.EnchantLimiterSettingsFactory;
import me.ponktacology.clashmc.guild.enchantmentlimiter.updater.packet.PacketEnchantLimiterSettingsUpdate;

public class EnchantLimiterSettingsUpdater extends SettingsUpdater<EnchantLimiterSettings, PacketEnchantLimiterSettingsUpdate> {
  public EnchantLimiterSettingsUpdater(
      DataService dataService,
      NetworkService networkService,
      EnchantLimiterSettingsCache settingsCache,
      EnchantLimiterSettingsFactory settingsFactory) {
    super(
        dataService,
        networkService,
        PacketEnchantLimiterSettingsUpdate.class,
        settingsCache,
        settingsFactory);
  }
}

package me.ponktacology.clashmc.guild.player.combattag.global.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.guild.player.combattag.global.CombatTagSettings;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import me.ponktacology.clashmc.guild.player.combattag.global.factory.CombatTagSettingsFactory;
import me.ponktacology.clashmc.guild.player.combattag.global.updater.packet.PacketCombatTagSettingsUpdate;

public class CombatTagSettingsUpdater extends SettingsUpdater<CombatTagSettings, PacketCombatTagSettingsUpdate> {
  public CombatTagSettingsUpdater(
      DataService dataService,
      NetworkService networkService,
      CombatTagSettingsCache settingsCache,
      CombatTagSettingsFactory settingsFactory) {
    super(
        dataService,
        networkService,
        PacketCombatTagSettingsUpdate.class,
        settingsCache,
        settingsFactory);
  }
}

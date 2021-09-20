package me.ponktacology.clashmc.guild.guild.settings.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;
import me.ponktacology.clashmc.guild.guild.settings.cache.GuildSettingsCache;
import me.ponktacology.clashmc.guild.guild.settings.factory.GuildSettingsFactory;
import me.ponktacology.clashmc.guild.guild.settings.updater.packet.PacketGuildSettingsUpdate;

public class GuildSettingsUpdater extends SettingsUpdater<GuildSettings, PacketGuildSettingsUpdate> {
  public GuildSettingsUpdater(
      DataService dataService,
      NetworkService networkService,
      GuildSettingsCache settingsCache,
      GuildSettingsFactory settingsFactory) {
    super(
        dataService,
        networkService,
        PacketGuildSettingsUpdate.class,
        settingsCache,
        settingsFactory);
  }
}

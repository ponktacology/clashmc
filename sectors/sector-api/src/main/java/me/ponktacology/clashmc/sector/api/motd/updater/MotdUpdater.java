package me.ponktacology.clashmc.sector.api.motd.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.sector.api.motd.MotdSettings;
import me.ponktacology.clashmc.sector.api.motd.cache.MotdSettingsCache;
import me.ponktacology.clashmc.sector.api.motd.factory.MotdSettingsFactory;
import me.ponktacology.clashmc.sector.api.motd.updater.packet.PacketMotdUpdate;

public class MotdUpdater extends SettingsUpdater<MotdSettings, PacketMotdUpdate> {

  public MotdUpdater(
      DataService dataService,
      NetworkService networkService,
      MotdSettingsCache settingsCache,
      MotdSettingsFactory settingsFactory) {
    super(dataService, networkService, PacketMotdUpdate.class, settingsCache, settingsFactory);
  }
}

package me.ponktacology.clashmc.sector.api.whitelist.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.sector.api.whitelist.WhitelistSettings;
import me.ponktacology.clashmc.sector.api.whitelist.cache.WhitelistSettingsCache;
import me.ponktacology.clashmc.sector.api.whitelist.factory.WhitelistSettingsFactory;
import me.ponktacology.clashmc.sector.api.whitelist.updater.packet.PacketWhitelistUpdate;

public class WhitelistUpdater extends SettingsUpdater<WhitelistSettings, PacketWhitelistUpdate> {

  public WhitelistUpdater(
      DataService dataService,
      NetworkService networkService,
      WhitelistSettingsCache settingsCache,
      WhitelistSettingsFactory settingsFactory) {
    super(dataService, networkService, PacketWhitelistUpdate.class, settingsCache, settingsFactory);
  }
}

package me.ponktacology.clashmc.drop.turbo.global.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.drop.turbo.global.GlobalTurbo;
import me.ponktacology.clashmc.drop.turbo.global.cache.GlobalTurboCache;
import me.ponktacology.clashmc.drop.turbo.global.factory.GlobalTurboFactory;
import me.ponktacology.clashmc.drop.turbo.global.updater.packet.PacketGlobalTurboUpdate;

public class GlobalTurboUpdater extends SettingsUpdater<GlobalTurbo, PacketGlobalTurboUpdate> {
  public GlobalTurboUpdater(
      DataService dataService,
      NetworkService networkService,
      GlobalTurboCache settingsCache,
      GlobalTurboFactory settingsFactory) {
    super(
        dataService,
        networkService,
        PacketGlobalTurboUpdate.class,
        settingsCache,
        settingsFactory);
  }
}

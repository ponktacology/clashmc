package me.ponktacology.clashmc.queue.settings.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.queue.settings.QueueSettings;
import me.ponktacology.clashmc.queue.settings.cache.QueueSettingsCache;
import me.ponktacology.clashmc.queue.settings.factory.QueueSettingsFactory;
import me.ponktacology.clashmc.queue.settings.updater.packet.PacketQueueSettingsUpdate;

public class QueueSettingsUpdater extends SettingsUpdater<QueueSettings, PacketQueueSettingsUpdate> {
  public QueueSettingsUpdater(
      DataService dataService,
      NetworkService networkService,
      QueueSettingsCache settingsCache,
      QueueSettingsFactory settingsFactory) {
    super(
        dataService,
        networkService,
        PacketQueueSettingsUpdate.class,
        settingsCache,
        settingsFactory);
  }
}

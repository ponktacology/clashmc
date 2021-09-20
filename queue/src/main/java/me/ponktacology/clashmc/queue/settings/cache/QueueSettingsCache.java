package me.ponktacology.clashmc.queue.settings.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.queue.settings.QueueSettings;
import me.ponktacology.clashmc.queue.settings.factory.QueueSettingsFactory;

public class QueueSettingsCache extends SettingsCache<QueueSettings> {
  public QueueSettingsCache(QueueSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

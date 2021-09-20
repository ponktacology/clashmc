package me.ponktacology.clashmc.queue.settings.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.queue.settings.QueueSettings;

public class QueueSettingsFactory extends SettingsFactory<QueueSettings> {
  public QueueSettingsFactory(DataService dataService) {
    super(dataService, QueueSettings.class);
  }
}

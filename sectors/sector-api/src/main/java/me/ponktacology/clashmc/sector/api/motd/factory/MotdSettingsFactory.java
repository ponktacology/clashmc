package me.ponktacology.clashmc.sector.api.motd.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.sector.api.motd.MotdSettings;

public class MotdSettingsFactory extends SettingsFactory<MotdSettings> {
  public MotdSettingsFactory(DataService dataService) {
    super(dataService, MotdSettings.class);
  }
}

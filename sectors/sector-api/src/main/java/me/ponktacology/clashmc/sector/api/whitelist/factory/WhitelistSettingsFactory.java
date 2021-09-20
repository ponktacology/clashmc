package me.ponktacology.clashmc.sector.api.whitelist.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.sector.api.whitelist.WhitelistSettings;

public class WhitelistSettingsFactory extends SettingsFactory<WhitelistSettings> {
  public WhitelistSettingsFactory(DataService dataService) {
    super(dataService, WhitelistSettings.class);
  }
}

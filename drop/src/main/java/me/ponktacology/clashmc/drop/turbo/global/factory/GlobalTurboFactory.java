package me.ponktacology.clashmc.drop.turbo.global.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.drop.turbo.global.GlobalTurbo;

public class GlobalTurboFactory extends SettingsFactory<GlobalTurbo> {
  public GlobalTurboFactory(DataService dataService) {
    super(dataService, GlobalTurbo.class);
  }
}

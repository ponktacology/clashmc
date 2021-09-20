package me.ponktacology.clashmc.guild.guild.settings.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;

public class GuildSettingsFactory extends SettingsFactory<GuildSettings> {
  public GuildSettingsFactory(DataService dataService) {
    super(dataService, GuildSettings.class);
  }
}

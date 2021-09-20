package me.ponktacology.clashmc.core.chat.global.settings.factory;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;

public class ChatSettingsFactory extends SettingsFactory<ChatSettings> {
  public ChatSettingsFactory(DataService dataService) {
    super(dataService, ChatSettings.class);
  }
}

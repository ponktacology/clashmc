package me.ponktacology.clashmc.core.chat.global.settings.cache;

import me.ponktacology.clashmc.api.settings.cache.SettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;
import me.ponktacology.clashmc.core.chat.global.settings.factory.ChatSettingsFactory;

public class ChatSettingsCache extends SettingsCache<ChatSettings> {

  public ChatSettingsCache(ChatSettingsFactory settingsFactory) {
    super(settingsFactory);
  }
}

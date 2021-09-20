package me.ponktacology.clashmc.core.chat.global.settings.updater;

import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.settings.updater.SettingsUpdater;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.factory.ChatSettingsFactory;
import me.ponktacology.clashmc.core.chat.global.settings.updater.packet.PacketChatSettingsUpdate;

public class ChatSettingsUpdater extends SettingsUpdater<ChatSettings, PacketChatSettingsUpdate> {
  public ChatSettingsUpdater(
      DataService dataService,
      NetworkService networkService,
      ChatSettingsCache settingsCache,
      ChatSettingsFactory settingsFactory) {
    super(
        dataService,
        networkService,
        PacketChatSettingsUpdate.class,
        settingsCache,
        settingsFactory);
  }
}

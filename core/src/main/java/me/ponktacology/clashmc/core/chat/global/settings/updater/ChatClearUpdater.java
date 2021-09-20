package me.ponktacology.clashmc.core.chat.global.settings.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.updater.packet.PacketChatClearUpdate;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;


import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ChatClearUpdater implements Updater {


  private final NetworkService networkService;

  private final ChatSettingsCache chatSettingsCache;

  public void update() {
    this.update(chatSettingsCache.get());
  }


  public void update(ChatSettings entity) {
    this.networkService.publish(new PacketChatClearUpdate());
  }

  @RequiredArgsConstructor
  public static class ChatClearUpdateListener implements PacketListener {


    private final CorePlayerCache playerCache;

    @PacketHandler
    public void onChatClearUpdate(PacketChatClearUpdate packet) {
      for (CorePlayer player :
          this.playerCache.values().stream()
              .filter(it -> !it.isStaff())
              .collect(Collectors.toList())) {
        for (int i = 0; i < 100; i++) {
          player.sendMessage(" ");
        }
      }
    }
  }
}

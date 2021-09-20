package me.ponktacology.clashmc.core.chat.global.announcer;

import me.ponktacology.clashmc.core.chat.global.event.GlobalChatEvent;
import me.ponktacology.clashmc.core.chat.global.packet.PacketGlobalChatAnnounce;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.PluginUtil;
import org.bukkit.Bukkit;

import java.util.Optional;

public class LocalChatAnnouncer implements GlobalChatAnnouncer {

  private final CorePlayerCache playerCache;

  public LocalChatAnnouncer(CorePlayerCache playerCache) {
    this.playerCache = playerCache;
  }

  @Override
  public void announce( GlobalChatEvent event) {
    PluginUtil.callEvent(event);

    if (!event.isCancelled()) {
      Optional<CorePlayer> playerOptional = playerCache.get(event.getPlayer());

      if (!playerOptional.isPresent()) {
        return;
      }

      CorePlayer corePlayer = playerOptional.get();

      String formatted =
              String.format(event.getFormat(), corePlayer.getName(), event.getMessage());

      Bukkit.getServer().broadcastMessage(formatted);
    }
  }

  @Override
  public void handle(PacketGlobalChatAnnounce announce) {}
}

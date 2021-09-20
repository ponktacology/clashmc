package me.ponktacology.clashmc.core.chat.global.announcer;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.core.chat.global.event.GlobalChatEvent;
import me.ponktacology.clashmc.core.chat.global.packet.PacketGlobalChatAnnounce;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class GlobalChatAnnouncerImpl implements GlobalChatAnnouncer {


  private final NetworkService networkService;

  private final CorePlayerCache playerCache;

  @Override
  public void announce( GlobalChatEvent event) {
    PluginUtil.callEvent(event);

    if (!event.isCancelled()) {
      this.networkService.publish(
          new PacketGlobalChatAnnounce(
              event.getPlayer().getUniqueId(), event.getFormat(), event.getMessage()));
    }
  }

  @Override
  public void handle( PacketGlobalChatAnnounce packet) {
    Optional<CorePlayer> corePlayerOptional = this.playerCache.get(packet.getSender());

    if (!corePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer corePlayer = corePlayerOptional.get();

    String message =
        String.format(
            Text.colored(packet.getFormat()), corePlayer.getName(), packet.getMessage());

    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      player.sendMessage(message);
    }
  }

  @RequiredArgsConstructor
  public static class GlobalChatAnnounceListener implements PacketListener {


    private final GlobalChatAnnouncer announcer;

    @PacketHandler
    public void onGlobalChatAnnounce(PacketGlobalChatAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}

package me.ponktacology.clashmc.core.player.privatemessage.announcer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.privatemessage.PrivateMessage;
import me.ponktacology.clashmc.core.player.privatemessage.announcer.packet.PacketPrivateMessageAnnounce;
import me.ponktacology.clashmc.core.player.privatemessage.event.PlayerPrivateMessageEvent;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PrivateMessageAnnouncer
    implements Announcer<PrivateMessage, PacketPrivateMessageAnnounce> {


  private final NetworkService networkService;

  private final CorePlayerCache playerCache;

  @Override
  public void announce( PrivateMessage message) {
    CorePlayer sender = message.getSender();
    CorePlayer receiver = message.getReceiver();
    PlayerPrivateMessageEvent event =
        new PlayerPrivateMessageEvent(sender, receiver, ChatColor.stripColor(message.getMessage()));

    PluginUtil.callEvent(event);

    if (!event.isCancelled()) {
      message
          .getSender()
          .sendMessage(
              Text.colored(
                  "&8[&eJa &8-> &e"
                      + message.getReceiver().getFormattedName()
                      + "&8] &7"
                      + event.getMessage()));

      this.networkService.publish(
          new PacketPrivateMessageAnnounce(
              sender.getUuid(), receiver.getUuid(), message.getMessage()));
    }
  }

  @Override
  public void handle( PacketPrivateMessageAnnounce packet) {
    Optional.ofNullable(Bukkit.getPlayer(packet.getReceiver()))
        .ifPresent(
            it -> {
              Optional<CorePlayer> corePlayerOptional = this.playerCache.get(it);

              if (!corePlayerOptional.isPresent()) {
                log.info(
                    "Received private message announce but player not in cache, player= "
                        + it.getName());
                return;
              }

              Optional<CorePlayer> senderCorePlayerOptional =
                  this.playerCache.get(packet.getSender());

              if (!senderCorePlayerOptional.isPresent()) {
                log.info(
                    "Received private message announce but sender not in cache, player= "
                        + packet.getSender());
                return;
              }

              CorePlayer senderCorePlayer = senderCorePlayerOptional.get();

              String message = packet.getMessage();
              CorePlayer corePlayer = corePlayerOptional.get();

              corePlayer.setReplyPlayer(packet.getSender());
              corePlayer.sendMessage(
                  Text.colored(
                      "&8[&e"
                          + senderCorePlayer.getFormattedName()
                          + " &8-> &eJa&8] &7"
                          + message));
            });
  }

  @RequiredArgsConstructor
  public static class PrivateMessageAnnounceListener implements PacketListener {


    private final PrivateMessageAnnouncer announcer;

    @PacketHandler
    public void onPacketPrivateMessageAnnounce( PacketPrivateMessageAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}

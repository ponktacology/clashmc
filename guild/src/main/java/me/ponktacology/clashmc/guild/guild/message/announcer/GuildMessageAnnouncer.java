package me.ponktacology.clashmc.guild.guild.message.announcer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.message.GuildMessage;
import me.ponktacology.clashmc.guild.guild.message.announcer.packet.PacketGuildMessageAnnounce;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;


import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GuildMessageAnnouncer implements Announcer<GuildMessage, PacketGuildMessageAnnounce> {


  private final NetworkService networkService;

  private final GuildCache guildCache;

  private final GuildPlayerCache guildPlayerCache;

  @Override
  public void announce( GuildMessage message) {
    announce(message, GuildMessage.MessageType.GUILD);
  }

  public void announce( GuildMessage message, GuildMessage.MessageType type) {
    this.networkService.publish(
        new PacketGuildMessageAnnounce(
            message.getSender().getUuid(),
            message.getGuild().getTag(),
            message.getMessage(),
            type));
  }

  @Override
  public void handle( PacketGuildMessageAnnounce packet) {
    Optional<Guild> guildOptional = this.guildCache.getByTag(packet.getGuild());

    if (!guildOptional.isPresent()) {
      log.info(
          "Received guild message but guild not found in database, guild= " + packet.getGuild());
      return;
    }

    Guild guild = guildOptional.get();
    Optional<GuildPlayer> guildPlayerOptional = this.guildPlayerCache.get(packet.getSender());

    if (!guildPlayerOptional.isPresent()) {
      log.info(
          "Received guild message but player not found in database, player= " + packet.getSender());
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    switch (packet.getMessageType()) {
      case GUILD:
        guild.broadcast(
            "&8[&2Gildia&8] &2" + guildPlayer.getName() + "&8: &2" + packet.getMessage());
        break;
      case ALLY:
        String formattedMessage =
            "&8[&9Sojusz&8] &8[&9"
                + guild.getTag()
                + "&8] &9"
                + guildPlayer.getName()
                + "&8: &9"
                + packet.getMessage();
        guild.broadcast(formattedMessage);
        guild.allies().forEach(it -> it.broadcast(formattedMessage));
        break;
    }
  }

  @RequiredArgsConstructor
  public static class GuildMessageAnnounceListener implements PacketListener {


    private final GuildMessageAnnouncer announcer;

    @PacketHandler
    public void onGuildMessageAnnounce( PacketGuildMessageAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}

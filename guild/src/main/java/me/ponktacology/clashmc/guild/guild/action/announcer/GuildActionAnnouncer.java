package me.ponktacology.clashmc.guild.guild.action.announcer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.action.GuildAction;
import me.ponktacology.clashmc.guild.guild.action.GuildActionWrapper;
import me.ponktacology.clashmc.guild.guild.action.announcer.packet.PacketGuildActionAnnounce;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class GuildActionAnnouncer implements Announcer<GuildAction, PacketGuildActionAnnounce> {

  private final NetworkService networkService;

  private final GuildPlayerCache playerCache;

  private final GuildCache guildCache;

  @Override
  public void announce(GuildAction action) {
    Guild guild = action.getGuild();
    guild.addAction(action);
    guild.save();

    this.networkService.publish(
        new PacketGuildActionAnnounce(
            action.getType(), action.getGuild().getTag(), action.getPlayer().getUuid()));
  }

  @Override
  public void handle(PacketGuildActionAnnounce packet) {
    String tag = packet.getGuild();
    UUID playerUUID = packet.getPlayer();

    if (tag == null || playerUUID == null) {
      return;
    }

    Optional<Guild> guildOptional = this.guildCache.getByTag(tag);

    if (!guildOptional.isPresent()) {
      log.info(
          "GuildActionAnnounce received but guild not found in database, guild= " + tag);
      return;
    }

    Guild guild = guildOptional.get();

    guild.addAction(
        new GuildActionWrapper(packet.getType(), packet.getPlayer(), packet.getGuild()));

    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(playerUUID);

    if (!guildPlayerOptional.isPresent()) {
      log.info(
          "GuildActionAnnounce received but guild player not found in database, player= "
              + playerUUID);
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    for (GuildPlayer otherGuildPlayer : this.playerCache.values()) {
      if (!otherGuildPlayer.isChatSettingEnabled(ChatSettings.Settings.GUILD_MESSAGES)) continue;

      String playerTag;

      if (guild.hasMember(otherGuildPlayer)) {
        playerTag = "&8[&2" + guild.getTag() + "&8]";
      } else if (guild.hasAlly(otherGuildPlayer)) {
        playerTag = "&8[&9" + guild.getTag() + "&8]";
      } else playerTag = "&8[&c" + guild.getTag() + "&8]";

      switch (packet.getType()) {
        case PLAYER_JOIN:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&aGracz &7"
                      + guildPlayer.getName()
                      + " &adołączył do gildii "
                      + playerTag
                      + "."));
          break;
        case PLAYER_LEAVE:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&cGracz &7" + guildPlayer.getName() + " &copuścił gildię " + playerTag + "."));
          break;
        case PLAYER_KICK:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&cGracz &7"
                      + guildPlayer.getName()
                      + " &czostał wyrzucony z gildii "
                      + playerTag
                      + "."));
          break;
        case CREATE:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&aGracz &7"
                      + guildPlayer.getName()
                      + " &azałożył gildię "
                      + playerTag
                      + " - &7"
                      + guild.getName()
                      + "."));
          break;
      }
    }
  }

  @RequiredArgsConstructor
  public static class GuildActionAnnounceListener implements PacketListener {

    private final GuildActionAnnouncer announcer;

    @PacketHandler
    public void onGuildActionAnnounce(PacketGuildActionAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}

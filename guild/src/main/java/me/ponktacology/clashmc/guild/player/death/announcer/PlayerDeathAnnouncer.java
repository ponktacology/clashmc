package me.ponktacology.clashmc.guild.player.death.announcer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;
import me.ponktacology.clashmc.guild.player.death.PlayerDeath;
import me.ponktacology.clashmc.guild.player.death.announcer.packet.PacketPlayerDeathAnnounce;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PlayerDeathAnnouncer implements Announcer<PlayerDeath, PacketPlayerDeathAnnounce> {


  private final NetworkService networkService;

  private final GuildPlayerCache playerCache;

  @Override
  public void announce( PlayerDeath death) {
    this.networkService.publish(
        new PacketPlayerDeathAnnounce(
            death.getPlayer().getUuid(),
            death.getKiller() == null ? null : death.getKiller().getUuid(),
            death.getRank()));
  }

  @Override
  public void handle( PacketPlayerDeathAnnounce packet) {
    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(packet.getPlayer());

    if (!guildPlayerOptional.isPresent()) {
      log.info(
          "Received death announce but GuildPlayer not found in database, player= "
              + packet.getPlayer().toString());
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    if (packet.getKiller() == null) {
      for (GuildPlayer otherGuildPlayer : this.playerCache.values()) {
        if (!otherGuildPlayer.isChatSettingEnabled(ChatSettings.Settings.DEATH_MESSAGES)) continue;

        otherGuildPlayer.sendMessage(
            Text.colored("&cGracz " + guildPlayer.getName() + " zginął."));
      }
    } else {
      Optional<GuildPlayer> killerGuildPlayerOptional = this.playerCache.get(packet.getKiller());

      if (!killerGuildPlayerOptional.isPresent()) {
        log.info(
            "Received death announce but killer GuildPlayer not found in database, player= "
                + packet.getKiller().toString());
        return;
      }

      GuildPlayer killerGuildPlayer = killerGuildPlayerOptional.get();

      for (GuildPlayer otherGuildPlayer : this.playerCache.values()) {
        if (!otherGuildPlayer.isChatSettingEnabled(ChatSettings.Settings.DEATH_MESSAGES)) continue;

        String playerTag = "";

        if (guildPlayer.hasGuild()) {
          Guild guild = guildPlayer.getGuild().get();

          if (guild.hasMember(otherGuildPlayer)) {
            playerTag = "&8[&2" + guild.getTag() + "&8] ";
          } else if (guild.allies().stream().anyMatch(it -> it.hasMember(otherGuildPlayer))) {
            playerTag = "&8[&9" + guild.getTag() + "&8] ";
          } else playerTag = "&8[&c" + guild.getTag() + "&8] ";
        }

        String killerTag = "";

        if (killerGuildPlayer.hasGuild()) {
          Guild guild = killerGuildPlayer.getGuild().get();

          if (guild.hasMember(otherGuildPlayer)) {
            killerTag = "&8[&2" + guild.getTag() + "&8] ";
          } else if (guild.allies().stream().anyMatch(it -> it.hasMember(otherGuildPlayer))) {
            killerTag = "&8[&9" + guild.getTag() + "&8] ";
          } else killerTag = "&8[&c" + guild.getTag() + "&8] ";
        }

        String message =
            playerTag
                + "&7"
                + guildPlayer.getName()
                + " &8(&c-"
                + packet.getRank()
                + "&8) &czostał zabity przez "
                + killerTag
                + "&7"
                + killerGuildPlayer.getName()
                + " &8(&a+"
                + packet.getRank()
                + "&8)";

        otherGuildPlayer.sendMessage(Text.colored(message));
      }
    }
  }

  @RequiredArgsConstructor
  public static class PlayerDeathAnnounceListener implements PacketListener {


    private final PlayerDeathAnnouncer announcer;

    @PacketHandler
    public void onPlayerDeathAnnounce( PacketPlayerDeathAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}

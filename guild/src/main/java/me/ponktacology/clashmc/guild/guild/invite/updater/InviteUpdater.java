package me.ponktacology.clashmc.guild.guild.invite.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.action.ActionType;
import me.ponktacology.clashmc.guild.guild.action.GuildAction;
import me.ponktacology.clashmc.guild.guild.action.announcer.GuildActionAnnouncer;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.invite.GuildInvite;
import me.ponktacology.clashmc.guild.guild.invite.updater.packet.PacketGuildInviteUpdate;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;


import java.util.Optional;

@RequiredArgsConstructor
public class InviteUpdater implements Updater {

  private final NetworkService networkService;
  private final GuildActionAnnouncer guildActionAnnouncer;

  
  public void update( GuildInvite invite) {
    this.networkService.publish(
        new PacketGuildInviteUpdate(
            invite.getPlayer().getUuid(), invite.getGuild().getTag(), invite.isAccepted()));

    if (invite.isAccepted()) {
      this.guildActionAnnouncer.announce(
          new GuildAction(ActionType.PLAYER_JOIN, invite.getPlayer(), invite.getGuild()));
    }

    GuildPlayer guildPlayer = invite.getPlayer();

    if (!CorePlugin.INSTANCE.getPlayerCache().isOnlineNotInAuthOrLobby(guildPlayer)) {
      Guild guild = invite.getGuild();

      if (invite.isAccepted()) {
        guild.addMember(guildPlayer);
        guild.save();
      } else {
        guild.addToInvited(guildPlayer);
      }
    }
  }

  @Slf4j
  @RequiredArgsConstructor
  public static class InviteUpdateListener implements PacketListener {

    private final GuildCache guildCache;
    private final GuildPlayerCache playerCache;

    @PacketHandler
    public void onGuildInviteUpdate( PacketGuildInviteUpdate packet) {
      Optional<Guild> guildOptional = this.guildCache.getByTag(packet.getGuild());

      if (!guildOptional.isPresent()) {
        log.info(
            "Received guild invite update but guild not found in cache, guild= "
                + packet.getGuild());
        return;
      }

      Guild guild = guildOptional.get();

      Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(packet.getPlayer());

      if (!guildPlayerOptional.isPresent()) {
        log.info(
            "Received guild invite update but player not found in cache, player="
                + packet.getPlayer());
        return;
      }

      GuildPlayer guildPlayer = guildPlayerOptional.get();

      if (!packet.isAccepted()) {
        guild.addToInvited(guildPlayer);
      } else {
        guild.addMember(guildPlayer);
      }
    }
  }
}

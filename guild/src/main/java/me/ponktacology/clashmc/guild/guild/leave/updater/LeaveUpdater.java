package me.ponktacology.clashmc.guild.guild.leave.updater;

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
import me.ponktacology.clashmc.guild.guild.leave.GuildLeave;
import me.ponktacology.clashmc.guild.guild.leave.updater.packet.PacketGuildLeaveUpdate;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;


import java.util.Optional;

@RequiredArgsConstructor
public class LeaveUpdater implements Updater {

  
  private final NetworkService networkService;
  
  private final GuildActionAnnouncer guildActionAnnouncer;

  
  public void update( GuildLeave leave) {
    this.networkService.publish(
        new PacketGuildLeaveUpdate(
            leave.getPlayer().getUuid(), leave.getGuild().getTag(), leave.isKick()));
    this.guildActionAnnouncer.announce(
        new GuildAction(
            leave.isKick() ? ActionType.PLAYER_KICK : ActionType.PLAYER_LEAVE,
            leave.getPlayer(),
            leave.getGuild()));

    GuildPlayer guildPlayer = leave.getPlayer();

    if (!CorePlugin.INSTANCE.getPlayerCache().isOnlineNotInAuthOrLobby(guildPlayer)) {
      Guild guild = leave.getGuild();

      guild.removeMember(guildPlayer);
      guild.save();

      guildPlayer.save();
    }
  }

  @Slf4j
  @RequiredArgsConstructor
  public static class LeaveUpdateListener implements PacketListener {

    
    private final GuildCache guildCache;
    
    private final GuildPlayerCache playerCache;

    @PacketHandler
    public void onGuildInviteUpdate( PacketGuildLeaveUpdate packet) {
      Optional<Guild> guildOptional = this.guildCache.getByTag(packet.getGuild());

      if (!guildOptional.isPresent()) {
        log.info(
            "Received guild leave update but guild not found in cache, guild= "
                + packet.getGuild());
        return;
      }

      Guild guild = guildOptional.get();

      Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(packet.getPlayer());

      if (!guildPlayerOptional.isPresent()) {
        log.info(
            "Received guild leave update but player not found in cache, player="
                + packet.getPlayer());
        return;
      }

      GuildPlayer guildPlayer = guildPlayerOptional.get();

      guild.removeMember(guildPlayer);
    }
  }
}

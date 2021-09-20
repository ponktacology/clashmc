package me.ponktacology.clashmc.guild.guild.war.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.action.ActionType;
import me.ponktacology.clashmc.guild.guild.action.GuildWarAction;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.war.GuildWar;
import me.ponktacology.clashmc.guild.guild.war.action.GuildWarActionAnnouncer;
import me.ponktacology.clashmc.guild.guild.war.updater.packet.PacketGuildWarRemove;
import me.ponktacology.clashmc.guild.guild.war.updater.packet.PacketGuildWarUpdate;
import me.ponktacology.clashmc.guild.guild.war.wrapper.GuildWarWrapper;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GuildWarUpdater implements Updater {

  private final NetworkService networkService;
  private final GuildWarActionAnnouncer announcer;
  private final GuildCache guildCache;

  public void update(GuildWar war) {
    this.networkService.publish(new PacketGuildWarUpdate(GuildWarWrapper.from(war)));
    this.announcer.announce(
        new GuildWarAction(ActionType.WAR_START, war.getGuild(), war.getEnemy()));
  }

  public void remove(Guild winner, Guild loser) {
    this.networkService.publish(new PacketGuildWarRemove(winner.getTag(), loser.getTag()));
    this.announcer.announce(
        new GuildWarAction(ActionType.WAR_WON, winner, loser));
  }

  @PacketHandler
  public void onPacket(PacketGuildWarRemove packet) {
    Optional<Guild> winnerOptional = this.guildCache.getByTag(packet.getWinner());

    if (!winnerOptional.isPresent()) {
      log.warn("Received guild war update but winner not found in a cache.");
      return;
    }

    Optional<Guild> loserOptional = this.guildCache.getByTag(packet.getLoser());

    if (!loserOptional.isPresent()) {
      log.warn("Received guild war update but enemy not found in a cache.");
      return;
    }

    Guild winner = winnerOptional.get();
    Guild loser = loserOptional.get();

    winner.removeWar(loser);
    loser.removeWar(winner);
  }

  @PacketHandler
  public void onPacket(PacketGuildWarUpdate packet) {
    GuildWarWrapper wrapper = packet.getWrapper();
    Optional<Guild> guildOptional = this.guildCache.getByTag(wrapper.getGuild());

    if (!guildOptional.isPresent()) {
      log.warn("Received guild war update but guild not found in a cache.");
      return;
    }

    Optional<Guild> enemyOptional = this.guildCache.getByTag(wrapper.getEnemy());

    if (!enemyOptional.isPresent()) {
      log.warn("Received guild war update but enemy not found in a cache.");
      return;
    }

    Guild guild = guildOptional.get();
    Guild enemy = enemyOptional.get();

    guild.addWar(GuildWar.from(wrapper, false));
    enemy.addWar(GuildWar.from(wrapper, true));
  }
}

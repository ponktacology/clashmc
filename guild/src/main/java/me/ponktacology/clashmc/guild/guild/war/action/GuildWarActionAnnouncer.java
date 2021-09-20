package me.ponktacology.clashmc.guild.guild.war.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.action.ActionType;
import me.ponktacology.clashmc.guild.guild.action.GuildWarAction;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.war.action.packet.PacketGuildWarActionAnnounce;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class GuildWarActionAnnouncer
    implements Announcer<GuildWarAction, PacketGuildWarActionAnnounce> {

  private final NetworkService networkService;
  private final GuildPlayerCache playerCache;
  private final GuildCache guildCache;

  @Override
  public void announce(GuildWarAction action) {
    Guild guild = action.getGuild();
    guild.addAction(action);
    guild.save();

    Guild enemy = action.getEnemy();
    enemy.addAction(
        new GuildWarAction(
            action.getType() == ActionType.WAR_WON ? ActionType.WAR_LOST : action.getType(),
            enemy,
            guild));
    enemy.save();

    this.networkService.publish(
        new PacketGuildWarActionAnnounce(
            action.getType(), action.getGuild().getTag(), action.getEnemy().getTag()));
  }

  @PacketHandler
  @Override
  public void handle(PacketGuildWarActionAnnounce packet) {
    String guildTag = packet.getGuild();
    String enemyTag = packet.getEnemy();

    Optional<Guild> guildOptional = this.guildCache.getByTag(guildTag);

    if (!guildOptional.isPresent()) {
      log.info("GuildActionAnnounce received but guild not found in database, guild= " + guildTag);
      return;
    }

    Guild guild = guildOptional.get();

    Optional<Guild> enemyOptional = this.guildCache.getByTag(enemyTag);

    if (!enemyOptional.isPresent()) {
      log.info("GuildActionAnnounce received but guild not found in database, enemy= " + enemyTag);
      return;
    }

    Guild enemy = enemyOptional.get();

    guild.addAction(new GuildWarAction(packet.getType(), guild, enemy));
    enemy.addAction(
        new GuildWarAction(
            packet.getType() == ActionType.WAR_WON ? ActionType.WAR_LOST : packet.getType(),
            enemy,
            guild));

    for (GuildPlayer otherGuildPlayer : this.playerCache.values()) {
      if (!otherGuildPlayer.isChatSettingEnabled(ChatSettings.Settings.GUILD_MESSAGES)) continue;

      String guildTagFormatted;
      String enemyTagFormatted;

      if (guild.hasMember(otherGuildPlayer)) {
        guildTagFormatted = "&8[&2" + guild.getTag() + "&8]";
      } else if (guild.hasAlly(otherGuildPlayer)) {
        guildTagFormatted = "&8[&9" + guild.getTag() + "&8]";
      } else guildTagFormatted = "&8[&c" + guild.getTag() + "&8]";

      if (enemy.hasMember(otherGuildPlayer)) {
        enemyTagFormatted = "&8[&2" + enemy.getTag() + "&8]";
      } else if (guild.hasAlly(otherGuildPlayer)) {
        enemyTagFormatted = "&8[&9" + enemy.getTag() + "&8]";
      } else enemyTagFormatted = "&8[&c" + enemy.getTag() + "&8]";

      assert !guildTagFormatted.equals(enemyTagFormatted);

      switch (packet.getType()) {
        case CONQUER:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&aGildia &7"
                      + guildTagFormatted
                      + " &apodbiła gildię "
                      + enemyTagFormatted
                      + "."));
          break;
        case WAR_WON:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&aGildia &7"
                      + guildTagFormatted
                      + " &awygrała wojnę z gildią "
                      + enemyTagFormatted
                      + "."));
          break;
        case WAR_START:
          otherGuildPlayer.sendMessage(
              Text.colored(
                  "&aGildia &7"
                      + guildTagFormatted
                      + " &arozpoczęła wojnę z gildią "
                      + enemyTagFormatted
                      + "."));
          break;
      }
    }
  }
}

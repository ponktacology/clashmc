package me.ponktacology.clashmc.guild.guild.war;

import lombok.Data;
import lombok.ToString;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.war.wrapper.GuildWarWrapper;

import java.util.Optional;

@Data
@ToString(callSuper = true)
public class GuildWar implements Expiring {

  private final Guild guild;
  private final Guild enemy;
  private final long timeStamp;

  public GuildWar(Guild guild, Guild enemy, long timeStamp) {
    this.guild = guild;
    this.enemy = enemy;
    this.timeStamp = timeStamp;
  }

  public GuildWar(Guild guild, Guild enemy) {
    this.guild = guild;
    this.enemy = enemy;
    this.timeStamp = System.currentTimeMillis();
  }

  public static GuildWar from(GuildWarWrapper wrapper, boolean enemy) {
    Optional<Guild> guildOptional =
        GuildPlugin.INSTANCE.getGuildCache().getByTag(wrapper.getGuild());

    Optional<Guild> enemyOptional =
        GuildPlugin.INSTANCE.getGuildCache().getByTag(wrapper.getEnemy());

    if (!guildOptional.isPresent() || !enemyOptional.isPresent()) return null;

    return guildOptional
        .map(
            it ->
                new GuildWar(
                    enemy ? enemyOptional.get() : guildOptional.get(),
                    enemy ? guildOptional.get() : enemyOptional.get(),
                    wrapper.getTimeStamp()))
        .orElse(null);
  }

  @Override
  public boolean hasExpired() {
    return System.currentTimeMillis() - this.timeStamp > GuildConstants.GUILD_WAR_DURATION_TIME;
  }

  public long getTimeLeft() {
    return GuildConstants.GUILD_WAR_DURATION_TIME - (System.currentTimeMillis() - this.timeStamp);
  }
}

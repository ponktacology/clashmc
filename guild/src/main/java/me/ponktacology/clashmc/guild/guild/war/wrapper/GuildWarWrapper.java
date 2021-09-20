package me.ponktacology.clashmc.guild.guild.war.wrapper;

import lombok.Data;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.guild.war.GuildWar;

@Data
public class GuildWarWrapper implements Expiring {

  private final String guild;
  private final String enemy;
  private final long timeStamp;

  public static GuildWarWrapper from(GuildWar war) {
    return new GuildWarWrapper(war.getGuild().getTag(), war.getEnemy().getTag(), war.getTimeStamp());
  }

  @Override
  public boolean hasExpired() {
    return System.currentTimeMillis() - this.timeStamp > GuildConstants.GUILD_WAR_DURATION_TIME;
  }
}

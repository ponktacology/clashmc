package me.ponktacology.clashmc.guild.guild;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.statistics.tracked.murder.MurderTrackedStatistic;

import java.util.Set;

@Getter
public class GuildStatistics {

  private int rank = 1000;
  private int killStreak;
  private long lastKill = 0L;
  private final Set<MurderTrackedStatistic> kills = Sets.newConcurrentHashSet();
  private final Set<MurderTrackedStatistic> deaths = Sets.newConcurrentHashSet();

  public void increaseKillStreak() {
    this.killStreak++;
  }

  public void increaseRank(int rank) {
    this.rank += rank;
  }

  public void decreaseRank(int rank) {
    this.rank = Math.max(0, this.rank - rank);
  }

  public void increaseKills(GuildPlayer killer, GuildPlayer victim, int eloChange) {
    this.lastKill = System.currentTimeMillis();
    this.increaseKillStreak();
    this.kills.add(new MurderTrackedStatistic(killer, victim, eloChange));
  }

  public void increaseDeaths(GuildPlayer killer, GuildPlayer victim, int eloChange) {
    this.killStreak = 0;
    this.deaths.add(new MurderTrackedStatistic(killer, victim, eloChange));
  }

  public int kills() {
    return this.kills.size();
  }

  public int deaths() {
    return this.deaths.size();
  }

  public int killStreak() {
    return this.killStreak;
  }
}

package me.ponktacology.clashmc.guild.player.statistics;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.statistics.tracked.murder.MurderTrackedStatistic;

import java.util.Set;

@Getter
@Setter
public class PlayerStatistics {

  private static final long POSITION_CACHE_EVICTION_TIME = 10_000L;

  private int rank = 1000;
  private int position;
  private int usedPearls;
  private int hits;
  private int killStreak;
  private double dealtDamage;
  private double receivedDamage;

  @Getter(AccessLevel.NONE)
  private int playTime;

  private int killingSpree;
  private long lastKill = 0L;

  private final Set<MurderTrackedStatistic> kills = Sets.newConcurrentHashSet();
  private final Set<MurderTrackedStatistic> deaths = Sets.newConcurrentHashSet();
  private final Set<MurderTrackedStatistic> assists = Sets.newConcurrentHashSet();

  public void increaseKillStreak() {
    this.killStreak++;
  }

  public int increaseKillingSpree() {
    return (System.currentTimeMillis() - this.lastKill < GuildConstants.KILLING_SPREE_TIME)
        ? ++this.killingSpree
        : (this.killingSpree = 1);
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
    this.killingSpree = 0;
    this.deaths.add(new MurderTrackedStatistic(killer, victim, eloChange));
  }

  public void increaseAssists(GuildPlayer assister, GuildPlayer victim, int eloChange) {
    this.assists.add(new MurderTrackedStatistic(assister, victim, eloChange));
  }

  public int increaseUsedPearls() {
    return ++this.usedPearls;
  }

  public double increaseDealtDamage(double damage) {
    return this.dealtDamage += damage;
  }

  public double increaseReceivedDamage(double damage) {
    return this.receivedDamage += damage;
  }

  public int kills() {
    return this.kills.size();
  }

  public int deaths() {
    return this.deaths.size();
  }

  public int assists() {
    return this.assists.size();
  }

  public double getKD() {
    double deaths = this.deaths();
    double kills = this.kills();

    return MathUtil.roundOff(kills / (deaths == 0 ? 1 : deaths > kills ? -deaths : deaths), 2);
  }

  public void addPlayTime(GuildPlayer guildPlayer) {
    long loginTime =
        CorePlugin.INSTANCE
            .getPlayerCache()
            .get(guildPlayer)
            .map(CorePlayer::getLoginTime)
            .orElse(System.currentTimeMillis());

    this.playTime += (System.currentTimeMillis() - loginTime);
  }

  public long getPlayTime(GuildPlayer guildPlayer) {
    long loginTime =
        CorePlugin.INSTANCE
            .getPlayerCache()
            .get(guildPlayer)
            .map(CorePlayer::getLoginTime)
            .orElse(System.currentTimeMillis());

    return this.playTime
        + (CorePlugin.INSTANCE.getPlayerCache().isOnlineNotInAuthOrLobby(guildPlayer)
            ? (System.currentTimeMillis() - loginTime)
            : 0);
  }

  public void reset() {
    this.kills.clear();
    this.deaths.clear();
    this.assists.clear();
    this.killStreak = 0;
    this.killingSpree = 0;
    this.rank = 1000;
  }
}

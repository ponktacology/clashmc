package me.ponktacology.clashmc.guild.player.statistics.tracked.murder;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.statistics.tracked.TrackedStatistic;


import java.util.UUID;

@RequiredArgsConstructor
public class MurderTrackedStatistic extends TrackedStatistic {

  private final UUID killer;
  private final UUID victim;
  private final int rankChange;

  public MurderTrackedStatistic( GuildPlayer killer,  GuildPlayer victim, int rankChange) {
    this.killer = killer.getUuid();
    this.victim = victim.getUuid();
    this.rankChange = rankChange;
  }
}

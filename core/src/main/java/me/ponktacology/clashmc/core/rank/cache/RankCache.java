package me.ponktacology.clashmc.core.rank.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.core.CoreConstants;
import me.ponktacology.clashmc.core.rank.Rank;


import java.util.List;

public final class RankCache extends IgnoreCaseKeyValueCache<Rank> {

  public void add( Rank rank) {
    this.add(rank.getName(), rank);
  }

  public Rank defaultRank() {
    return get(CoreConstants.DEFAULT_RANK_NAME).orElse(null);
  }

  public void addAll( List<Rank> ranks) {
    for (Rank rank : ranks) {
      add(rank);
    }
  }
}

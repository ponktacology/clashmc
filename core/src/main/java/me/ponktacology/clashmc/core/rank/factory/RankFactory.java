package me.ponktacology.clashmc.core.rank.factory;

import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.core.CoreConstants;
import me.ponktacology.clashmc.core.rank.Rank;


import java.util.List;
import java.util.Optional;

public final class RankFactory implements Factory<Rank> {

  private final DataService dataService;

  public RankFactory(DataService dataService) {
    this.dataService = dataService;
  }


  public Rank create(String name) {
    return new Rank(name);
  }

  public Optional<Rank> load(String name) {
    return this.dataService.load(name, Rank.class);
  }


  public List<Rank> loadAll() {
    List<Rank> ranks = this.dataService.loadAll(Rank.class);

    if (ranks.isEmpty()) {
      Rank rank = new Rank(CoreConstants.DEFAULT_RANK_NAME);
      rank.save();
      ranks.add(rank);
    }

    return ranks;
  }

  public void delete(Rank rank) {
    this.dataService.delete(rank);
  }
}

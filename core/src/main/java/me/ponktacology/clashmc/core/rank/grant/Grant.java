package me.ponktacology.clashmc.core.rank.grant;

import lombok.Getter;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.managable.Manageable;
import me.ponktacology.clashmc.core.rank.Rank;

import java.util.Optional;
import java.util.UUID;

@Getter
public class Grant extends Manageable {

  private final String rankName;

  public Grant(String rankName, UUID addedBy, String reason, long addedOn, long duration) {
    super(addedBy, reason, addedOn, duration);
    this.rankName = rankName;
  }

  public Optional<Rank> getRank() {
    return CorePlugin.INSTANCE.getRankCache().get(this.rankName);
  }
}

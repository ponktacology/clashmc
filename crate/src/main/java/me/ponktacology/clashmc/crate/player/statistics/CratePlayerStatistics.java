package me.ponktacology.clashmc.crate.player.statistics;

import lombok.Getter;

@Getter
public class CratePlayerStatistics {

  private int openedNormalCrate = 0;
  private int openedPremiumCrate = 0;

  public void incrementOpenedNormalCrate() {
    this.openedNormalCrate++;
  }

  public void incrementOpenedPremiumCrate() {
    this.openedPremiumCrate++;
  }
}

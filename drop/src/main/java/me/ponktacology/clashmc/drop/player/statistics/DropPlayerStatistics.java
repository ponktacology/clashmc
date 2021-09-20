package me.ponktacology.clashmc.drop.player.statistics;

import lombok.Getter;

@Getter
public class DropPlayerStatistics {

  private int minedStone;
  private int minedObsidian;

  public void incrementMinedStone() {
    this.minedStone++;
  }

  public void incrementMinedObsidian() {
    this.minedObsidian++;
  }
}

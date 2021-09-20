package me.ponktacology.clashmc.drop.player.data;

import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.drop.DropConstants;

@Getter
@Setter
public class PlayerDropData {

  private boolean enabled = true;
  private int level;

  public boolean isMaxLevel() {
    return DropConstants.MAX_DROP_LEVEL <= this.level;
  }

  public int getUpgradePrice() {
    return (this.level + 1) * 4000;
  }

  public double getBonus() {
    return (this.level * 0.05D);
  }

  public void increaseLevel() {
    this.level++;
  }

  public void toggle() {
    this.enabled = !this.enabled;
  }
}

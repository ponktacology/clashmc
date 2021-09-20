package me.ponktacology.clashmc.drop.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.player.data.PlayerDropData;
import me.ponktacology.clashmc.drop.player.statistics.DropPlayerStatistics;
import me.ponktacology.clashmc.drop.turbo.Turbo;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(collection = "players", database = "drop")
public class DropPlayer extends BukkitPlayerWrapper {

  private final Map<String, PlayerDropData> dropData = new HashMap<>();
  private final DropPlayerStatistics statistics = new DropPlayerStatistics();
  private final Turbo turboDrop = new Turbo();
  private final Turbo turboExp = new Turbo();

  private int points;
  private boolean mineCobblestone = true;

  public DropPlayer(UUID uuid,  String name) {
    super(uuid, name);
  }

  public boolean isItemEnabled( DropItem item) {
    PlayerDropData dropData = getDropData(item);

    return dropData.isEnabled();
  }

  public void toggleItem( DropItem item) {
    PlayerDropData dropData = getDropData(item);

    dropData.toggle();
  }

  public PlayerDropData getDropData( DropItem item) {
    return this.dropData.computeIfAbsent(
        item.getName().toUpperCase(Locale.ROOT), it -> new PlayerDropData());
  }

  public void setTurboDropDuration(long duration) {
    this.turboDrop.setDuration(duration);
  }

  public void setTurboExpDuration(long duration) {
    this.turboExp.setDuration(duration);
  }

  public void decreasePoints(int points) {
    this.points -= points;
  }

  public void incrementPoints() {
    this.points += MathUtil.random(0, 5);
  }

  public void disableAll() {
    dropData.values().forEach(it -> it.setEnabled(false));
  }

  public void enableAll() {
    dropData.values().forEach(it -> it.setEnabled(true));
  }

  public void toggleMineCobblestone() {
    this.mineCobblestone = !mineCobblestone;
  }

  public boolean hasTurboDrop() {
    return !this.turboDrop.hasExpired();
  }

  public boolean hasTurboExp() {
    return !this.turboExp.hasExpired();
  }

  public double getDropMultiplier() {
    double multiplier = this.getRankMultiplier();

    if (hasTurboDrop()) {
      multiplier += 1;
    }

    return multiplier;
  }

  public double getExpMultiplier() {
    double multiplier = this.getRankMultiplier();

    if (hasTurboExp()) {
      multiplier += 1;
    }

    return multiplier;
  }

  private double getRankMultiplier() {
    CorePlayer corePlayer = CorePlugin.INSTANCE.getPlayerCache().getOrKick(this.getPlayer());

    if (corePlayer.getMainRank().getPower() > 0) {
      return 0.5;
    }

    return 0;
  }
}

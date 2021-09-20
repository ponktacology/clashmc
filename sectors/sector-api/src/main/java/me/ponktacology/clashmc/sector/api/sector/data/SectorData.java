package me.ponktacology.clashmc.sector.api.sector.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ponktacology.clashmc.sector.api.SectorConstants;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SectorData {

  private int players;
  private int maxPlayers;
  private double[] tps;
  private double cpuUsage;
  private int ramUsage;
  private int maxRam;
  private long startTime;

  private final transient long lastUpdate = System.currentTimeMillis();

  public boolean isResponding() {
    return System.currentTimeMillis() - this.lastUpdate < SectorConstants.MAX_RESPOND_TIME;
  }

  public boolean isReady() {
    return System.currentTimeMillis() - this.startTime > SectorConstants.START_TIME;
  }

  public boolean isAvailable() {
    return this.isReady() && this.isResponding();
  }
}

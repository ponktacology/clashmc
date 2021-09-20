package me.ponktacology.clashmc.sector.sector.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.SystemUtil;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.data.SectorData;
import me.ponktacology.clashmc.sector.api.sector.updater.SectorUpdater;
import org.bukkit.Bukkit;


@RequiredArgsConstructor
public class SectorUpdateTask implements Runnable {

  
  private final Sector localSector;
  
  private final SectorUpdater sectorUpdater;
  private final long startTime = System.currentTimeMillis();

  // CPU usage method is very heavy and should be cached
  @Getter private static double cachedCPUUsage = SystemUtil.getCPUUsageProcess();
  private long lastCPUUsageFetch = System.currentTimeMillis();

  @Override
  public void run() {
    if (System.currentTimeMillis() - this.lastCPUUsageFetch > 7_000L) {
      cachedCPUUsage = SystemUtil.getCPUUsageProcess();
      this.lastCPUUsageFetch = System.currentTimeMillis();
    }

    SectorData data =
        new SectorData(
            Bukkit.getServer().getOnlinePlayers().size(),
            Bukkit.getMaxPlayers(),
            Bukkit.getServer().spigot().getTPS(),
            cachedCPUUsage,
            (int) SystemUtil.getFreeRam(),
            (int) SystemUtil.getMaxRam(),
            this.startTime);

    this.localSector.setData(data);
    this.sectorUpdater.update(this.localSector);
  }
}

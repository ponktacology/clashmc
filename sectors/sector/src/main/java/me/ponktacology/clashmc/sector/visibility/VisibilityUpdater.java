package me.ponktacology.clashmc.sector.visibility;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.sector.task.SectorUpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class VisibilityUpdater implements Updater {


  private final TaskDispatcher taskDispatcher;

  
  public void update( Sector sector) {
    double cpuUsage = SectorUpdateTask.getCachedCPUUsage();

    int viewDistance = Math.max((int) (7 - (cpuUsage * 12)), 1);

    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      try {
        player.spigot().setViewDistance(viewDistance);

        if (!sector.isSpawn()) {
          continue;
        }

        if (cpuUsage < 0.7) {
          continue;
        }

        this.taskDispatcher.runAsync(
            () -> {
              for (Player other : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getLocation().distanceSquared(other.getLocation()) <= 4) {
                  player.showPlayer(other);
                  other.showPlayer(player);
                } else {
                  player.hidePlayer(other);
                  other.hidePlayer(player);
                }
              }
            });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

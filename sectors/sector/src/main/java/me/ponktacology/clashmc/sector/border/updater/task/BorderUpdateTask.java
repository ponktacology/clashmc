package me.ponktacology.clashmc.sector.border.updater.task;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.border.updater.BorderUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class BorderUpdateTask implements Runnable {


  private final BorderUpdater borderUpdater;

  @Override
  public void run() {
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      try {
        this.borderUpdater.update(player);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

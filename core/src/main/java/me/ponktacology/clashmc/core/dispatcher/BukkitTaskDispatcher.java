package me.ponktacology.clashmc.core.dispatcher;

import me.ponktacology.clashmc.api.dispatcher.ExecutorTaskDispatcher;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public final class BukkitTaskDispatcher extends ExecutorTaskDispatcher {

  @Override
  public void runAsync(Runnable runnable) {
    if (!Bukkit.isPrimaryThread()) {
      runnable.run();
      return;
    }

    super.runAsync(runnable);
  }

  @Override
  public void run(Runnable runnable) {
    if (Bukkit.isPrimaryThread()) {
      runnable.run();
      return;
    }

    CorePlugin.INSTANCE
        .getPlugin()
        .getServer()
        .getScheduler()
        .runTask(CorePlugin.INSTANCE.getPlugin(), runnable);
  }

  @Override
  public void runLater(Runnable runnable, long time, TimeUnit unit) {
    CorePlugin.INSTANCE
        .getPlugin()
        .getServer()
        .getScheduler()
        .runTaskLater(
            CorePlugin.INSTANCE.getPlugin(), runnable, TimeUtil.convertTimeToTicks(time, unit));
  }

  @Override
  public void runTimer(Runnable runnable, long time, TimeUnit unit) {
    CorePlugin.INSTANCE
        .getPlugin()
        .getServer()
        .getScheduler()
        .runTaskTimer(
            CorePlugin.INSTANCE.getPlugin(), runnable, 0, TimeUtil.convertTimeToTicks(time, unit));
  }
}

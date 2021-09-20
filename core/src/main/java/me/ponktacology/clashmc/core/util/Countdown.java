package me.ponktacology.clashmc.core.util;

import me.ponktacology.clashmc.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Countdown {

  private int taskId;
  private boolean cancelled = false;

  public Countdown(int time, boolean decrement) {
    this(time, decrement, true);
  }

  public Countdown(int time, boolean decrement, boolean async) {
    final AtomicInteger counter = new AtomicInteger(decrement ? time : 0);

    BukkitRunnable runnable =
        new BukkitRunnable() {
          @Override
          public void run() {
            if (cancelled) {
              cancel();
              return;
            }

            int seconds = decrement ? counter.decrementAndGet() : counter.incrementAndGet();

            if ((decrement && seconds == 0) || seconds > time) {
              cancel();
              onFinish();
              return;
            }

            onTick(seconds);
          }
        };

    this.taskId =
        (async
                ? runnable.runTaskTimerAsynchronously(CorePlugin.INSTANCE.getPlugin(), 0L, 20L)
                : runnable.runTaskTimer(CorePlugin.INSTANCE.getPlugin(), 0L, 20L))
            .getTaskId();
  }

  public abstract void onTick(int time);

  public abstract void onFinish();

  public void cancel() {
    this.cancelled = true;
    try {
      if (this.taskId != -1 && Bukkit.getScheduler().isCurrentlyRunning(taskId)) {
        Bukkit.getScheduler().cancelTask(taskId);
      }
    } catch (Exception ignored) {

    }
  }
}

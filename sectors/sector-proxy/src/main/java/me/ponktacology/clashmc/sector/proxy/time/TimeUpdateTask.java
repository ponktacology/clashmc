package me.ponktacology.clashmc.sector.proxy.time;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.api.sector.time.TimeUpdater;


@RequiredArgsConstructor
public class TimeUpdateTask implements Runnable {


  private final TimeUpdater timeUpdater;

  private long ticks;

  @Override
  public void run() {
    this.ticks += 1000;

    if (this.ticks > 24000) {
      this.ticks = 0;
    }

    timeUpdater.update(ticks);
  }
}

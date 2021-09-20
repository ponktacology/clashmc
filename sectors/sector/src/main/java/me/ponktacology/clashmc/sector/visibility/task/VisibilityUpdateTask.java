package me.ponktacology.clashmc.sector.visibility.task;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.visibility.VisibilityUpdater;


@RequiredArgsConstructor
public class VisibilityUpdateTask implements Runnable {


  private final VisibilityUpdater visibilityUpdater;

  private final Sector localSector;

  @Override
  public void run() {
    this.visibilityUpdater.update(this.localSector);
  }
}

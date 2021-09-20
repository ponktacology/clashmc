package me.ponktacology.clashmc.sector.sector.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;


@RequiredArgsConstructor
public class SectorMenuFactory {


  private final SectorCache sectorCache;

  private final CorePlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  private final PlayerTransferUpdater transferUpdater;

  private final Sector localSector;


  public SectorMenu getSectorMenu() {
    return new SectorMenu(this.sectorCache);
  }


  public ChannelMenu getChannelMenu() {
    return new ChannelMenu(this.sectorCache, this.playerCache, this.taskDispatcher, this.transferUpdater, this.localSector);
  }
}

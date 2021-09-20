package me.ponktacology.clashmc.sector.api.sector.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.api.sector.updater.packet.PacketSectorRemove;
import me.ponktacology.clashmc.sector.api.sector.updater.packet.PacketSectorUpdate;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SectorUpdater implements Updater {

  private final NetworkService networkService;

  private final DataService dataService;

  public void update(Sector sector) {
    sector.save(this.dataService);
    this.networkService.publish(new PacketSectorUpdate(sector.getName()));
  }

  public void remove(Sector sector) {
    networkService.publish(new PacketSectorRemove(sector.getName()));
  }

  @RequiredArgsConstructor
  public static class SectorUpdaterListener implements PacketListener {

    private final DataService dataService;

    private final SectorCache sectorCache;

    @PacketHandler
    public void onPacketSectorUpdate(PacketSectorUpdate packet) {
      Optional<Sector> optionalSector = this.dataService.load(packet.getSector(), Sector.class);

      if (!optionalSector.isPresent()) {
        log.info(
            "Received sector update but sector not in database sector= " + packet.getSector());
        return;
      }

      Sector sector = optionalSector.get();

      this.sectorCache.add(sector);
    }

    @PacketHandler
    public void onPacketSectorRemove(PacketSectorRemove packet) {
      this.sectorCache.remove(packet.getSector());
    }
  }
}

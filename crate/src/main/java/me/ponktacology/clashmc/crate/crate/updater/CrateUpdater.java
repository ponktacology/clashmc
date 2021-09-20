package me.ponktacology.clashmc.crate.crate.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.crate.crate.factory.CrateFactory;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketCrateRemove;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketCrateUpdate;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CrateUpdater implements Updater {

  private final NetworkService networkService;
  private final CrateFactory crateFactory;

  public void update( Crate crate) {
    crate.save();
    this.networkService.publish(new PacketCrateUpdate(crate.getName()));
  }

  
  public void remove( Crate crate) {
    this.crateFactory.delete(crate);
    this.networkService.publish(new PacketCrateRemove(crate.getName()));
  }

  @RequiredArgsConstructor
  public static class CrateUpdateListener implements PacketListener {

    private final CrateFactory crateFactory;
    private final CrateCache crateCache;

    @PacketHandler
    public void onPacketUpdate( PacketCrateUpdate packet) {
      Optional<Crate> crateOptional = this.crateFactory.load(packet.getName());

      if (!crateOptional.isPresent()) {
        log.info(
            "Received crate update but crate not in database crate= " + packet.getName());
        return;
      }

      Crate crate = crateOptional.get();

      this.crateCache.add(crate);
    }

    @PacketHandler
    public void onPacketRemove( PacketCrateRemove packet) {
      this.crateCache.remove(packet.getName());
    }
  }
}

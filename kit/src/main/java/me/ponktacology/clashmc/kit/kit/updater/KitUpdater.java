package me.ponktacology.clashmc.kit.kit.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.kit.kit.Kit;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.kit.factory.KitFactory;
import me.ponktacology.clashmc.kit.kit.updater.packet.PacketKitRemove;
import me.ponktacology.clashmc.kit.kit.updater.packet.PacketKitUpdate;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class KitUpdater implements Updater {

  
  private final KitFactory kitFactory;
  
  private final NetworkService networkService;

  
  public void update( Kit kit) {
    kit.save();
    this.networkService.publish(new PacketKitUpdate(kit.getName()));
  }

  
  public void remove( Kit kit) {
    this.kitFactory.delete(kit);
    this.networkService.publish(new PacketKitRemove(kit.getName()));
  }

  @RequiredArgsConstructor
  public static final class KitUpdateListener implements PacketListener {

    
    private final KitFactory kitFactory;
    
    private final KitCache kitCache;

    @PacketHandler
    public void onKitUpdate( PacketKitUpdate packet) {
      Optional<Kit> kitOptional = this.kitFactory.load(packet.getName());

      if (!kitOptional.isPresent()) {
        log.info(
            "Kit update received but kit not found in database kit= " + packet.getName());
        return;
      }

      Kit kit = kitOptional.get();

      this.kitCache.add(kit);
    }

    @PacketHandler
    public void onKitRemove( PacketKitRemove packet) {
      this.kitCache.remove(packet.getName());
    }
  }
}

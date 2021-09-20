package me.ponktacology.clashmc.safe.item.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.safe.item.SafeItem;
import me.ponktacology.clashmc.safe.item.cache.SafeItemCache;
import me.ponktacology.clashmc.safe.item.factory.SafeItemFactory;
import me.ponktacology.clashmc.safe.item.updater.packet.PacketSafeItemRemove;
import me.ponktacology.clashmc.safe.item.updater.packet.PacketSafeItemUpdate;


import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SafeItemUpdater implements Updater {

  private final NetworkService networkService;
  private final SafeItemFactory safeItemFactory;
  
  public void update( SafeItem safeItem) {
    safeItem.save();
    this.networkService.publish(new PacketSafeItemUpdate(safeItem.getUuid()));
  }

  
  public void remove( SafeItem safeItem) {
    this.safeItemFactory.delete(safeItem);
    this.networkService.publish(new PacketSafeItemRemove(safeItem.getUuid()));
  }

  @RequiredArgsConstructor
  public static class SafeItemUpdateListener implements PacketListener {

    private final SafeItemFactory safeItemFactory;
    private final SafeItemCache safeItemCache;

    @PacketHandler
    public void onPacketUpdate( PacketSafeItemUpdate packet) {
      Optional<SafeItem> safeItemOptional = this.safeItemFactory.load(packet.getUuid());

      if (!safeItemOptional.isPresent()) {
        log.info(
            "Received safe item update but item not in database item= "
                + packet.getUuid().toString());
        return;
      }

      SafeItem safeItem = safeItemOptional.get();

      this.safeItemCache.add(safeItem);
    }

    @PacketHandler
    public void onPacketUpdate( PacketSafeItemRemove packet) {
      this.safeItemCache.remove(packet.getUuid());
    }
  }
}

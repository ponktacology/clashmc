package me.ponktacology.clashmc.drop.item.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.factory.DropItemFactory;
import me.ponktacology.clashmc.drop.item.updater.packet.PacketDropItemRemove;
import me.ponktacology.clashmc.drop.item.updater.packet.PacketDropItemUpdate;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class DropItemUpdater implements Updater {

  private final NetworkService networkService;
  private final DropItemFactory dropItemFactory;
  private final DropItemCache dropItemCache;

  
  public void update( DropItem item) {
    item.save();
    this.networkService.publish(new PacketDropItemUpdate(item.getName()));
  }

  
  public void remove( DropItem item) {
    this.dropItemFactory.delete(item);
    this.networkService.publish(new PacketDropItemRemove(item.getName()));
  }

  @PacketHandler
  public void onPacketUpdate( PacketDropItemUpdate packet) {
    Optional<DropItem> dropItemOptional = this.dropItemFactory.load(packet.getName());

    if (!dropItemOptional.isPresent()) {
      log.info(
          "Received drop item update but item not in database item= " + packet.getName());
      return;
    }

    DropItem dropItem = dropItemOptional.get();

    this.dropItemCache.add(dropItem);
  }

  @PacketHandler
  public void onPacketRemove( PacketDropItemRemove packet) {
    Optional<DropItem> dropItemOptional = this.dropItemCache.get(packet.getName());

    dropItemOptional.ifPresent(this.dropItemCache::remove);
  }
}

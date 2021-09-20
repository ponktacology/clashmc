package me.ponktacology.clashmc.sector.player.inventory.updater;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.Callback;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.sector.api.player.SectorPlayer;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.player.inventory.BukkitInventory;
import me.ponktacology.clashmc.sector.player.inventory.PlayerInventoryUpdate;
import me.ponktacology.clashmc.sector.player.inventory.cache.InventoryCache;
import me.ponktacology.clashmc.sector.player.inventory.updater.packet.PacketPlayerInventoryRequest;
import me.ponktacology.clashmc.sector.player.inventory.updater.packet.PacketPlayerInventoryResponse;
import me.ponktacology.clashmc.sector.player.inventory.updater.packet.PacketPlayerInventoryUpdate;
import me.ponktacology.clashmc.sector.player.util.PlayerUtil;
import org.bukkit.Bukkit;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class InventoryUpdater implements Updater {

  private final Map<UUID, Callback<BukkitInventory>> responses = Maps.newConcurrentMap();


  private final NetworkService networkService;

  private final DataService dataService;

  private final CorePlayerCache corePlayerCache;

  private final SectorPlayerFactory playerFactory;

  private final InventoryCache inventoryCache;

  
  public void update( PlayerInventoryUpdate update) {
    BukkitPlayerWrapper player = update.getPlayer();

    this.networkService.publish(
        new PacketPlayerInventoryUpdate(player.getUuid(), update.getUpdate()));

    if (!this.corePlayerCache.isOnlineNotInAuthOrLobby(player)) {
      SectorPlayer sectorPlayer =
          this.playerFactory.loadOrCreate(player.getUuid(), player.getName());
      sectorPlayer.addUpdate(update.getUpdate());
      sectorPlayer.save(this.dataService);
    }
  }

  public void request( BukkitPlayerWrapper sectorPlayer,  Callback<BukkitInventory> callback) {
    if (!this.corePlayerCache.isOnlineNotInAuthOrLobby(sectorPlayer)) {
      Optional<Inventory> optionalInventory = this.inventoryCache.get(sectorPlayer);

      if (!optionalInventory.isPresent()) {
        callback.accept(null);
      } else callback.accept(new BukkitInventory(optionalInventory.get()));

      return;
    }

    this.responses.put(sectorPlayer.getUuid(), callback);
    this.networkService.publish(new PacketPlayerInventoryRequest(sectorPlayer.getUuid()));
  }

  @PacketHandler
  public void onPacket( PacketPlayerInventoryRequest packet) {
    Optional.ofNullable(Bukkit.getPlayer(packet.getPlayer()))
        .ifPresent(
            it ->
                this.networkService.publish(
                    new PacketPlayerInventoryResponse(
                        packet.getPlayer(), PlayerUtil.wrapInventory(it))));
  }

  @PacketHandler
  public void onPacket( PacketPlayerInventoryResponse packet) {
    Optional.ofNullable(this.responses.remove(packet.getPlayer()))
        .ifPresent(it -> it.accept(new BukkitInventory(packet.getInventoryUpdate())));
  }

  @PacketHandler
  public void onPacket( PacketPlayerInventoryUpdate packet) {
    Optional.ofNullable(Bukkit.getPlayer(packet.getPlayer()))
        .ifPresent(it -> PlayerUtil.update(it, packet.getUpdate()));
  }
}

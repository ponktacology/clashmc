package me.ponktacology.clashmc.sector.player.inventory.cache;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.cache.Cache;
import me.ponktacology.clashmc.api.service.network.redis.RedisDataService;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import org.bukkit.entity.Player;


import java.util.Optional;

@RequiredArgsConstructor
public class InventoryCache implements Cache<Inventory> {

  
  private final RedisDataService dataService;

  public void add( Player player,  Inventory inventory) {
    this.dataService.set("inventory-cache", player.getUniqueId().toString(), inventory);
  }

  public void add( BukkitPlayerWrapper player,  Inventory inventory) {
    this.dataService.set("inventory-cache", player.getUuid().toString(), inventory);
  }

  public Optional<Inventory> get( BukkitPlayerWrapper player) {
    return this.dataService.get("inventory-cache", player.getUuid().toString(), Inventory.class);
  }
}

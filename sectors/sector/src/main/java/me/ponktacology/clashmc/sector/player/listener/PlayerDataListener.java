package me.ponktacology.clashmc.sector.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerQuitEvent;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.sector.api.player.SectorPlayer;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.player.info.PlayerInfo;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.border.Border;
import me.ponktacology.clashmc.sector.border.cache.BorderCache;
import me.ponktacology.clashmc.sector.player.inventory.cache.InventoryCache;
import me.ponktacology.clashmc.sector.player.teleport.cache.TeleportCache;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.player.util.PlayerUtil;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInitialSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class PlayerDataListener implements Listener {

  private static final ItemStack[] SPAWN_ITEMS = {
    new ItemBuilder(Material.STONE_PICKAXE)
        .enchantment(Enchantment.DURABILITY, 1)
        .enchantment(Enchantment.DIG_SPEED, 2)
        .build(),
    new ItemBuilder(Material.GRILLED_PORK).amount(64).build(),
    new ItemBuilder(Material.GRILLED_PORK).amount(64).build(),
    new ItemBuilder(Material.ENDER_CHEST).build()
  };

  private final TaskDispatcher taskDispatcher;
  private final SectorPlayerFactory playerFactory;
  private final BorderCache borderCache;
  private final Sector localSector;
  private final PlayerTransferUpdater transferUpdater;
  private final DataService dataService;
  private final TeleportCache teleportCache;
  private final PlayerTeleportUpdater teleportUpdater;
  private final BarManager barManager;
  private final InventoryCache inventoryCache;

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (this.localSector.isSpecial()) {
      return;
    }

    if (this.localSector.isSpawn()) {
      this.taskDispatcher.run(
          () -> me.ponktacology.clashmc.core.util.PlayerUtil.resetPlayer(player, false));
    }

    try {
      SectorPlayer sectorPlayer =
          this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());

      Border border = new Border(player.getUniqueId());

      this.borderCache.add(border);
      this.transferUpdater.setBeingDelayed(player, true);

      if (!sectorPlayer.isHasJoinedBefore()) {
        this.onFirstJoin(player);

        sectorPlayer.setHasJoinedBefore(true);
        sectorPlayer.save(this.dataService);
        return;
      }

      if (sectorPlayer.isTransfer()) {
        log.debug("Loading player inventory from database");

        PlayerInfo playerInfo = sectorPlayer.getInfo();

        if (playerInfo != null) {
          sectorPlayer.setTransfer(false);
          sectorPlayer.save(this.dataService);
          this.taskDispatcher.runLater(
              () -> PlayerUtil.unwrap(player, sectorPlayer.getInfo()), 100L, TimeUnit.MILLISECONDS);
        }
      }

      List<InventoryUpdate> pendingUpdates = sectorPlayer.getPendingUpdates();

      if (pendingUpdates.size() > 0) {
        pendingUpdates.forEach(it -> this.taskDispatcher.run(() -> PlayerUtil.update(player, it)));
        sectorPlayer.clearUpdates();
        sectorPlayer.save(this.dataService);
      }
    } catch (Exception e) {
      log.info("SectorPlayer not loaded properly player= " + player.getName());
      e.printStackTrace();
      event.disallow("&cNie udało się załadować twoich danych, skontaktuj się z administracją.");
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuitEvent(AsyncPlayerQuitEvent event) {
    Player player = event.getPlayer();

    try {
      this.teleportCache.removeAll(player);
      this.borderCache.remove(player);
      this.transferUpdater.setBeingDelayed(player, false);
      this.teleportUpdater.setBeingTeleported(player, false);
      this.transferUpdater.removeBeingTransferred(player);
      this.barManager.removeBars(player);

      Inventory inventory = PlayerUtil.wrapInventory(player);

      this.inventoryCache.add(player, inventory);
    } catch (Exception e) {
      log.info("Couldn't properly disconnect player= " + player.getName());
      e.printStackTrace();
    }
  }

  @EventHandler
  public void onPlayerSpawnLocationEvent(PlayerInitialSpawnEvent event) {
    Player player = event.getPlayer();

    if (!player.hasPlayedBefore()) {
      event.setSpawnLocation(RegionUtil.getRandomLocationInSector(this.localSector));
    }
  }

  private void onFirstJoin(Player player) {
    this.taskDispatcher.run(() -> player.getInventory().addItem(SPAWN_ITEMS));
  }
}

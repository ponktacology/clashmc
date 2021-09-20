package me.ponktacology.clashmc.sector.sector.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorConstants;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SectorProtectionListener implements Listener {


  private final PlayerTransferUpdater transferUpdater;

  private final PlayerTeleportUpdater teleportUpdater;

  private final Sector localSector;

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerTeleportEvent( PlayerTeleportEvent event) {
    if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

    Player player = event.getPlayer();

    if (!RegionUtil.isIn(event.getTo(), this.localSector)) {
      player.sendMessage(Text.colored("&cNie możesz się przeteleportować poza granice sektora."));
      player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onVehicleMoveEvent( VehicleMoveEvent event) {
    Vehicle vehicle = event.getVehicle();

    if (vehicle instanceof Slime || vehicle instanceof ArmorStand) {
      return;
    }

    Optional<Sector> sectorOptional = RegionUtil.getSectorIn(event.getTo());

    if (!sectorOptional.isPresent()) {
      event.getVehicle().teleport(event.getFrom());
      return;
    }

    Entity passenger = event.getVehicle().getPassenger();

    if (passenger instanceof Player) {
      if (!RegionUtil.isIn(event.getTo(), this.localSector)) {
        vehicle.remove();
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onExplosionPrimeTnt( ExplosionPrimeEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    Location location = event.getEntity().getLocation();

    if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
        && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityExplodeEvent( EntityExplodeEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    List<Block> toRemove = new ArrayList<>();

    for (Block block : event.blockList()) {
      if (RegionUtil.distanceToBorder(this.localSector, block.getLocation())
          <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
        toRemove.add(block);
        continue;
      }

      Location location = block.getLocation();

      if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
              && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
        toRemove.add(block);
      }
    }

    event.blockList().removeAll(toRemove);
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockIgniteEvent( BlockIgniteEvent event) {
    Player player = event.getPlayer();

    if (player == null) return;
    if (player.hasPermission("sector.buildbypass")) return;

    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    Location location = event.getBlock().getLocation();

    if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
            && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz podpalać bloków blisko spawna."));
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, location)
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz podpalać bloków blisko granicy."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockPlaceEvent( BlockPlaceEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission("sector.buildbypass")) return;

    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    Location location = event.getBlock().getLocation();

    if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
            && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz stawiać bloków tak blisko spawna."));
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, location)
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz stawiać bloków tak blisko granicy."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockBreakEvent( BlockBreakEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission("sector.buildbypass")) return;

    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    Location location = event.getBlock().getLocation();

    if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
            && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz niszczyć bloków tak blisko spawna."));
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, location)
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz niszczyć bloków tak blisko granicy."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockBurnEvent( BlockBurnEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    Location location = event.getBlock().getLocation();

    if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
            && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, location)
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityDamageEvent( EntityDamageEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    if (event.getEntity() instanceof Player) {
      if (this.transferUpdater.isBeingTransferred((Player) event.getEntity())) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityDamageEvent( EntityDamageByEntityEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    if (event.getEntity() instanceof Player) {
      if (this.transferUpdater.isBeingTransferred((Player) event.getEntity())) {
        event.setCancelled(true);
      }
    }

    if (event.getDamager() instanceof Player) {
      if (this.transferUpdater.isBeingTransferred((Player) event.getDamager())) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onEntityDamageEvent( EntityDamageByBlockEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    if (event.getEntity() instanceof Player) {
      if (this.transferUpdater.isBeingTransferred((Player) event.getEntity())) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerDropItemEvent( PlayerDropItemEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission("sector.buildbypass")) return;

    if (this.localSector.isSpawn()) {
      player.sendMessage(
          Text.colored(
              "&cNie możesz wyrzucać przedmiotów na spawnie. Użyj komendy /smietnik, aby pozbyć się przedmiotów lub /wymiana, aby zaprosić do wymiany innego gracza."));
      event.setCancelled(true);
      return;
    }

    if (this.transferUpdater.isBeingTransferred(player)
        || this.teleportUpdater.isBeingTeleported(player)) {
      player.sendMessage(Text.colored("&cNie możesz wyrzucać przedmiotów podczas teleportacji."));
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, player.getLocation())
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz wyrzucać przedmiotów tak blisko granicy."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerDropItemEvent( PlayerBucketEmptyEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission("sector.buildbypass")) return;

    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, player.getLocation())
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz wylewać wody tak blisko granicy."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerDropItemEvent( PlayerBucketFillEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission("sector.buildbypass")) return;

    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
      return;
    }

    if (RegionUtil.distanceToBorder(this.localSector, player.getLocation())
        <= SectorConstants.MIN_DISTANCE_ALLOW_BUILD) {
      player.sendMessage(Text.colored("&cNie możesz nabierać wody tak blisko granicy."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onPlayerCommandPreprocessEvent( PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();

    if (this.transferUpdater.isBeingTransferred(player)
        || this.teleportUpdater.isBeingTeleported(player)) {
      player.sendMessage(Text.colored("&cNie możesz używać komend podczas teleportacji."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBreakHangingEvent( HangingBreakByEntityEvent event) {
    Entity entity = event.getRemover();

    if (entity instanceof Player) {
      if (entity.hasPermission("sector.buildbypass")) return;
    }

    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBreakHangingEvent( HangingBreakEvent event) {
    if (this.localSector.isSpawn()) {
      event.setCancelled(true);
    }
  }
}

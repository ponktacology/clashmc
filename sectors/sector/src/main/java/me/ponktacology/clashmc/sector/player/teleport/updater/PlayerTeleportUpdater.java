package me.ponktacology.clashmc.sector.player.teleport.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorConstants;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.teleport.TeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.cache.TeleportCache;
import me.ponktacology.clashmc.sector.player.teleport.packet.PacketPlayerTeleportAccept;
import me.ponktacology.clashmc.sector.player.teleport.packet.PacketPlayerTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.request.LocationTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.request.PlayerTeleportRequest;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import me.ponktacology.clashmc.sector.util.TeleportCountdown;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
public class PlayerTeleportUpdater implements Updater {

  private final Set<UUID> teleportingPlayers = ConcurrentHashMap.newKeySet();
  private final NetworkService networkService;
  private final PlayerTransferUpdater transferUpdater;

  public boolean isBeingTeleported(Player player) {
    return this.teleportingPlayers.contains(player.getUniqueId());
  }

  public void setBeingTeleported(Player player, boolean teleported) {
    if (teleported) {
      this.teleportingPlayers.add(player.getUniqueId());
    } else {
      this.teleportingPlayers.remove(player.getUniqueId());
    }
  }

  public void update(TeleportRequest request) {
    if (request instanceof PlayerTeleportRequest) {
      PlayerTeleportRequest playerRequest = (PlayerTeleportRequest) request;
      this.networkService.publish(
          new PacketPlayerTeleportRequest(
              playerRequest.getSender().getUuid(), playerRequest.getReceiver().getUuid()));
    } else if (request instanceof LocationTeleportRequest) {
      LocationTeleportRequest locationRequest = (LocationTeleportRequest) request;
      CorePlayer corePlayer = locationRequest.getSender();
      Player player = corePlayer.getPlayer();

      if (player == null) {
        log.info("Teleport requested but player not on the server player=" + corePlayer.getName());
        return;
      }

      Location location = locationRequest.getLocation();
      Optional<Sector> sectorOptional = RegionUtil.getSectorIn(location);

      if (!sectorOptional.isPresent()) {
        log.info(
            "Teleport requested but location outside of the border, location= "
                + location.toString());
        return;
      }

      Sector sector = sectorOptional.get();

      if (corePlayer.isStaff()
          || corePlayer.hasPermission(SectorConstants.BYPASS_TELEPORT_COOLDOWN_PERMISSION)) {
        this.transferUpdater.update(player, sector, location);
      } else {
        new TeleportCountdown(corePlayer, sector, location);
      }
    }
  }

  public void accept(PlayerTeleportRequest request, Sector sector, Location location) {
    this.networkService.publish(
        new PacketPlayerTeleportAccept(
            request.getSender().getUuid(),
            request.getReceiver().getUuid(),
            sector.getName(),
            new Location(
                location.getWorld(),
                location.getX(),
                location.getBlockY(),
                location.getZ())));
  }

  @RequiredArgsConstructor
  @Slf4j
  public static class PlayerSectorTeleportUpdateListener implements PacketListener {

    private final CorePlayerCache playerCache;
    private final TeleportCache teleportCache;
    private final SectorCache sectorCache;
    private final PlayerTransferUpdater transferUpdater;
    private final PlayerTeleportUpdater teleportUpdater;
    private final Sector localSector;

    @PacketHandler
    public void onPlayerSectorTeleportAccept(PacketPlayerTeleportAccept packet) {
      Optional.ofNullable(Bukkit.getPlayer(packet.getSender()))
          .ifPresent(
              it -> {
                Optional<CorePlayer> corePlayerOptional = this.playerCache.get(it);

                if (!corePlayerOptional.isPresent()) {
                  log.info(
                      "Received teleport accept but player not in cache, player= " + it.getName());
                  return;
                }

                CorePlayer corePlayer = corePlayerOptional.get();

                Optional<Sector> sectorOptional = this.sectorCache.get(packet.getSector());

                if (!sectorOptional.isPresent()) {
                  log.info(
                      "Received teleport accept but sector not in cache, sector= "
                          + packet.getSector());
                  return;
                }

                Sector sector = sectorOptional.get();

                if (corePlayer.isStaff()
                    || corePlayer.hasPermission(
                        SectorConstants.BYPASS_TELEPORT_COOLDOWN_PERMISSION)) {
                  this.transferUpdater.update(it, sector, packet.getLocation());
                } else {
                  new TeleportCountdown(corePlayer, sector, packet.getLocation());
                }
              });
    }

    @PacketHandler
    public void onPlayerSectorTeleportRequest(PacketPlayerTeleportRequest packet) {
      Optional.ofNullable(Bukkit.getPlayer(packet.getReceiver()))
          .ifPresent(
              it -> {
                Optional<CorePlayer> senderCorePlayerOptional =
                    this.playerCache.get(packet.getSender());

                if (!senderCorePlayerOptional.isPresent()) {
                  log.info(
                      "Received teleport request but player not in database, player= "
                          + packet.getSender());
                  return;
                }

                CorePlayer senderCorePlayer = senderCorePlayerOptional.get();

                Optional<CorePlayer> receiverCorePlayerOptional = this.playerCache.get(it);

                if (!receiverCorePlayerOptional.isPresent()) {
                  log.info(
                      "Received teleport request but player not in cache, player= " + it.getName());
                  return;
                }

                CorePlayer receiverCorePlayer = receiverCorePlayerOptional.get();

                PlayerTeleportRequest request =
                    new PlayerTeleportRequest(senderCorePlayer, receiverCorePlayer);

                if (senderCorePlayer.isStaff()
                    || senderCorePlayer.hasPermission(
                        SectorConstants.BYPASS_TELEPORT_COOLDOWN_PERMISSION)) {
                  this.teleportUpdater.accept(
                      request, this.localSector, it.getPlayer().getLocation());
                } else {
                  this.teleportCache.add(request);

                  receiverCorePlayer.sendMessage(
                      Text.colored(
                          "&aOtrzymałeś prośbę o teleportację od gracza &7"
                              + senderCorePlayer.getFormattedName()
                              + "&a.\n&aWpisz &7/tpaccept "
                              + senderCorePlayer.getFormattedName()
                              + " &aaby zaakceptować prośbę."));
                }
              });
    }
  }
}

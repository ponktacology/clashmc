package me.ponktacology.clashmc.sector.player.teleport.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorConstants;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.player.teleport.cache.TeleportCache;
import me.ponktacology.clashmc.sector.player.teleport.request.LocationTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.request.PlayerTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class TeleportCommand {

  private final PlayerTeleportUpdater teleportUpdater;
  private final TeleportCache teleportCache;
  private final CorePlayerCache playerCache;
  private final Sector localSector;

  @Command(value = "tppos", description = "Wysyła prośbę o teleportację do gracza", async = true)
  @Permission(SectorConstants.TPPOS_PERMISSION)
  public void tppos(
      @Sender Player sender, @Name("x") double x, @Name("y") double y, @Name("z") double z) {
    Optional<CorePlayer> senderCorePlayerOptional = this.playerCache.get(sender);

    if (!senderCorePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer senderCorePlayer = senderCorePlayerOptional.get();

    this.teleportUpdater.update(
        new LocationTeleportRequest(
            senderCorePlayer, new Location(Bukkit.getWorld("world"), x, y, z)));
  }

  @Command(value = "tpa", description = "Wysyła prośbę o teleportację do gracza", async = true)
  @Permission(SectorConstants.TPA_PERMISSION)
  public void tpa(@Sender Player sender, @Name("player") CorePlayer corePlayer) {
    if (corePlayer.getUuid().equals(sender.getUniqueId())) {
      sender.sendMessage(
          Text.colored("&cNie możesz wysłać prośby o teleportację do samego siebie."));
      return;
    }

    if (!this.playerCache.isOnlineNotInAuthOrLobby(corePlayer)) {
      sender.sendMessage(Text.colored("&cGracz musi być online."));
      return;
    }

    if (this.teleportUpdater.isBeingTeleported(sender)) {
      sender.sendMessage(Text.colored("&cNie możesz użyć tej komendy podczas teleportacji."));
      return;
    }

    Optional<CorePlayer> senderCorePlayerOptional = this.playerCache.get(sender);

    if (!senderCorePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer senderCorePlayer = senderCorePlayerOptional.get();
    this.teleportUpdater.update(new PlayerTeleportRequest(senderCorePlayer, corePlayer));
    sender.sendMessage(Text.colored("&aPomyślnie wysłano prośbę o teleportację."));
  }

  @Command(
      value = "tpaccept all",
      description = "Akceptuje każdą prośbę o teleportację",
      async = true)
  public void tpaccept(@Sender Player sender) {
    CorePlayer senderCorePlayer = this.playerCache.getOrKick(sender);

    Set<PlayerTeleportRequest> requests = this.teleportCache.get(senderCorePlayer);

    for (PlayerTeleportRequest request : requests) {
      this.teleportUpdater.accept(request, localSector, sender.getLocation());
    }

    this.teleportCache.removeAll(senderCorePlayer);

    sender.sendMessage(Text.colored("&aPomyślnie zaakceptowano każdą prośbę o teleportację."));
  }

  @Command(value = "tpaccept", description = "Akceptuje prośbę o teleportację", async = true)
  public void tpaccept(@Sender Player sender, @Name("player") CorePlayer corePlayer) {
    if (!this.playerCache.isOnlineNotInAuthOrLobby(corePlayer)) {
      sender.sendMessage(Text.colored("&cGracz musi być online."));
      return;
    }

    CorePlayer senderCorePlayer = this.playerCache.getOrKick(sender);

    Optional<PlayerTeleportRequest> requestOptional =
        this.teleportCache.get(senderCorePlayer, corePlayer);

    if (!requestOptional.isPresent()) {
      sender.sendMessage(Text.colored("&cNie otrzymałeś prośby o teleportację od tego gracza."));
      return;
    }

    PlayerTeleportRequest request = requestOptional.get();

    this.teleportCache.remove(senderCorePlayer, corePlayer);
    this.teleportUpdater.accept(request, localSector, sender.getLocation());
    sender.sendMessage(Text.colored("&aPomyślnie zaakceptowano prośbę o teleportację."));
  }
}

package me.ponktacology.clashmc.sector.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.teleport.request.LocationTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class SpawnCommand {


  private final CorePlayerCache playerCache;

  private final PlayerTeleportUpdater teleportUpdater;
  private final Location spawnLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0, 90.0F, 0F);

  private final SectorCache sectorCache;

  @Command(value = "spawn", description = "Teleportuje na spawn", async = true)
  public void spawn( @Sender Player sender) {
    CorePlayer corePlayer = this.playerCache.getOrKick(sender);

    if (!this.sectorCache.getLeastCrowded(SectorType.SPAWN).isPresent()) {
      sender.sendMessage(Text.colored("&cNie znaleziono sektora spawn."));
      return;
    }

    this.teleportUpdater.update(new LocationTeleportRequest(corePlayer, this.spawnLocation));
  }
}

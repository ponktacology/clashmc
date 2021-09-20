package me.ponktacology.clashmc.sector.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PlayerTransferListener implements Listener {

  private final PlayerTransferUpdater transferUpdater;
  private final SectorCache sectorCache;
  private final CorePlayerCache playerCache;
  private final Sector localSector;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    Location from = event.getFrom();
    Location to = event.getTo();

    if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
      return;
    }

    Player player = event.getPlayer();

    if (player == null) {
      return;
    }

    if (!RegionUtil.isInIgnoreY(to, this.localSector)) {
      Optional<CorePlayer> corePlayerOptional = this.playerCache.get(player);

      if (!corePlayerOptional.isPresent()) {
        return;
      }

      if (this.transferUpdater.isBeingTransferred(player)) {
        return;
      }

      Optional<Sector> transferSectorOptional = RegionUtil.getSectorIn(this.sectorCache, to);

      if (!transferSectorOptional.isPresent()) {
        player.sendMessage(Text.colored("&cDotarłeś do granicy świata."));
        RegionUtil.knock(this.localSector, player);
        return;
      }

      final Sector finalTransferSector = transferSectorOptional.get();

      if (this.transferUpdater.isBeingDelayed(player)) {
        player.sendMessage(Text.colored("&cOdczekaj chwilę zanim znowu przejdziesz przez sektor."));
        RegionUtil.knock(this.localSector, player);
        return;
      }

      this.transferUpdater.update(player, finalTransferSector, to);
    }
  }
}

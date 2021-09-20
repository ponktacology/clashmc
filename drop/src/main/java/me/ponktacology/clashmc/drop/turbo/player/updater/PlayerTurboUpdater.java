package me.ponktacology.clashmc.drop.turbo.player.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.turbo.player.PlayerTurbo;
import me.ponktacology.clashmc.drop.turbo.player.updater.packet.PacketPlayerTurboUpdate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class PlayerTurboUpdater implements Updater {


  private final NetworkService networkService;

  private final DropPlayerCache playerCache;

  private final me.ponktacology.clashmc.core.player.cache.CorePlayerCache corePlayerCache;

  
  public void update( PlayerTurbo playerTurbo) {
    this.networkService.publish(
        new PacketPlayerTurboUpdate(
            playerTurbo.getPlayer().getUuid(),
            playerTurbo.isTurboDrop(),
            playerTurbo.getDuration()));

    DropPlayer dropPlayer = playerTurbo.getPlayer();

    if (!this.corePlayerCache.isOnlineNotInAuthOrLobby(dropPlayer)) {
      if (playerTurbo.isTurboDrop()) {
        dropPlayer.setTurboDropDuration(playerTurbo.getDuration());
      } else {
        dropPlayer.setTurboExpDuration(playerTurbo.getDuration());
      }
      dropPlayer.save();
    }
  }

  @PacketHandler
  public void onPacketUpdate( PacketPlayerTurboUpdate packet) {
    Player player = Bukkit.getPlayer(packet.getPlayer());
    if (player == null) return;

    DropPlayer dropPlayer = this.playerCache.getOrKick(player);

    if (packet.isTurboDrop()) {
      dropPlayer.setTurboDropDuration(packet.getDuration());
    } else {
      dropPlayer.setTurboExpDuration(packet.getDuration());
    }
  }
}

package me.ponktacology.clashmc.core.player.sync.service;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.Service;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.sync.PlayerSync;
import me.ponktacology.clashmc.core.player.sync.flag.SyncFlag;
import me.ponktacology.clashmc.core.player.sync.service.packet.PacketPlayerSync;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerSyncService implements Service, Updater, PacketListener {

  private final NetworkService networkService;
  private final BukkitPlayerCache<?> playerCache;

  public <V extends PlayerSync> void update(BukkitPlayerWrapper player, V playerSync) {
    if (playerSync.hasFlag(SyncFlag.SAVE_IF_NOT_ONLINE)) {
      if ((playerSync.hasFlag(SyncFlag.ONLINE_NOT_IN_AUTH_OR_LOBBY)
              && !this.playerCache.isOnlineNotInAuthOrLobby(player))
          || (playerSync.hasFlag(SyncFlag.ONLINE) && !this.playerCache.isOnline(player))) {
        playerSync.apply(player);
        player.save();
        return;
      }
    }

    this.networkService.publish(new PacketPlayerSync(player.getUuid(), playerSync));
  }

  @PacketHandler
  public void onPacket(PacketPlayerSync packet) {
    UUID uuid = packet.getPlayer();
    PlayerSync sync = packet.getSync();

    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(sync::apply);
  }
}

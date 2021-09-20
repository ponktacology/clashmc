package me.ponktacology.clashmc.crate.crate.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketAllPlayersCrateUpdate;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketPlayerCrateUpdate;
import me.ponktacology.clashmc.crate.player.CratePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PlayerCrateUpdater implements Updater {

  private final NetworkService networkService;
  private final CrateCache crateCache;

  public void update(CratePlayer cratePlayer, Crate crate, int amount) {
    this.networkService.publish(
        new PacketPlayerCrateUpdate(cratePlayer.getUuid(), crate.getName(), amount));
  }

  public void update(Crate crate, int amount) {
    this.networkService.publish(new PacketAllPlayersCrateUpdate(crate.getName(), amount));
  }

  @PacketHandler
  public void onPacketUpdate(PacketAllPlayersCrateUpdate packet) {
    Optional<Crate> crateOptional = this.crateCache.get(packet.getCrate());

    if (!crateOptional.isPresent()) {
      log.info("Received all players crate update but crate not found crate= " + packet.getCrate());
      return;
    }

    Crate crate = crateOptional.get();

    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      InventoryUtil.addItem(
          player, new ItemBuilder(crate.getCrateItem(packet.getAmount())).build());
    }
  }

  @PacketHandler
  public void onPacketUpdate(PacketPlayerCrateUpdate packet) {
    Player player = Bukkit.getPlayer(packet.getPlayer());
    if (player == null) return;

    Optional<Crate> crateOptional = this.crateCache.get(packet.getCrate());

    if (!crateOptional.isPresent()) {
      log.info(
          "Received crate player crate update but crate not found crate= " + packet.getCrate());
      return;
    }

    Crate crate = crateOptional.get();

    InventoryUtil.addItem(player, new ItemBuilder(crate.getCrateItem(packet.getAmount())).build());
  }
}

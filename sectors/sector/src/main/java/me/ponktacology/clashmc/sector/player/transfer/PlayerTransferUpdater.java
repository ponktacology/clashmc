package me.ponktacology.clashmc.sector.player.transfer;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.sector.SectorConstants;
import me.ponktacology.clashmc.sector.api.player.SectorPlayer;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.player.info.PlayerInfo;
import me.ponktacology.clashmc.sector.api.player.packet.PacketPlayerTransferRequest;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.player.transfer.event.AsyncPlayerSectorTransferEvent;
import me.ponktacology.clashmc.sector.player.util.PlayerUtil;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class PlayerTransferUpdater implements Updater {

  @SuppressWarnings("deprecated")
  private final Map<UUID, String> transferringPlayers = Maps.newConcurrentMap();

  private final Map<UUID, Long> lastTransfer = Maps.newConcurrentMap();
  private final SectorPlayerFactory playerFactory;
  private final Sector localSector;
  private final NetworkService networkService;
  private final DataService dataService;
  private final TaskDispatcher taskDispatcher;

  public void transfer(SectorPlayer sectorPlayer, Sector sector, PlayerInfo info) {
    sectorPlayer.setTransfer(true);
    sectorPlayer.setSector(sector);
    sectorPlayer.setInfo(info);
    sectorPlayer.save(this.dataService);

    this.networkService.publish(
        new PacketPlayerTransferRequest(sectorPlayer.getUuid(), sector.getName()));
  }

  public void update(Player player, Sector sector, Location location) {
    this.taskDispatcher.run(
        () -> {
          if (player == null) return;

          if (isBeingTransferred(player)) {
            return;
          }

          player.getOpenInventory().close();
          player.closeInventory();

          this.setBeingTransferred(player, sector);

          AsyncPlayerSectorTransferEvent event =
              new AsyncPlayerSectorTransferEvent(player, sector, location);

          CompletableFuture.supplyAsync(
                  () -> {
                    PluginUtil.callEvent(event);

                    return event;
                  })
              .thenAccept(
                  it -> {
                    if (it.isCancelled()) {
                      this.removeBeingTransferred(player);
                      return;
                    }

                    if (sector.getName().equals(this.localSector.getName())) {
                      player.teleport(location);
                      this.removeBeingTransferred(player);
                      return;
                    }

                    CraftPlayer cp = (CraftPlayer) player;
                    EntityPlayer ep = cp.getHandle();
                    PlayerConnection con = ep.playerConnection;

                    List<Entity> copyEntities = player.getWorld().getEntities();

                    for (Entity en : copyEntities) {
                      con.sendPacket(new PacketPlayOutEntityDestroy(en.getEntityId()));
                    }

                    Collection<Chunk> saveChunks = RegionUtil.getChunksAroundPlayer(player);
                    List<Chunk> copyChunks =
                        new ArrayList<>(Arrays.asList(player.getWorld().getLoadedChunks()));

                    for (Chunk chunk : copyChunks) {
                      if (saveChunks.contains(chunk)) {
                        continue;
                      }

                      net.minecraft.server.v1_8_R3.Chunk clearChunk =
                          new net.minecraft.server.v1_8_R3.Chunk(
                              ((CraftWorld) chunk.getWorld()).getHandle(),
                              new ChunkSnapshot(),
                              chunk.getX(),
                              chunk.getZ());

                      try {
                        con.sendPacket(new PacketPlayOutMapChunk(clearChunk, true, 0));
                      } catch (Exception e) {
                        e.printStackTrace();
                      }
                    }

                    PlayerInfo info = PlayerUtil.wrap(player);

                    info.x = location.getX();
                    info.y = location.getBlockY();
                    info.z = location.getZ();

                    this.taskDispatcher.runAsync(
                        () -> {
                          SectorPlayer sectorPlayer =
                              this.playerFactory.loadOrCreate(
                                  player.getUniqueId(), player.getName());
                          transfer(sectorPlayer, sector, info);
                        });

                    log.info(
                        "Sent transfer request, player= "
                            + player.getName()
                            + " sector= "
                            + sector.getName());
                  });
        });
  }

  public void setBeingDelayed(Player player, boolean delayed) {
    if (delayed) {
      this.lastTransfer.put(player.getUniqueId(), System.currentTimeMillis());
    } else {
      this.lastTransfer.remove(player.getUniqueId());
    }
  }

  public boolean isBeingDelayed(Player player) {
    if (!this.lastTransfer.containsKey(player.getUniqueId())) return false;

    return System.currentTimeMillis() - this.lastTransfer.get(player.getUniqueId())
        < SectorConstants.SECTOR_TRANSFER_DELAY_TIME;
  }

  public boolean isBeingTransferred(Player player) {
    return this.transferringPlayers.containsKey(player.getUniqueId());
  }

  public String getBeingTransferredTo(Player player) {
    return this.transferringPlayers.get(player.getUniqueId());
  }

  public void setBeingTransferred(Player player, Sector sector) {
    this.transferringPlayers.put(player.getUniqueId(), sector.getName());
  }

  public void removeBeingTransferred(Player player) {
    this.transferringPlayers.remove(player.getUniqueId());
  }
}

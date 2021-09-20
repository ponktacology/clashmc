package me.ponktacology.clashmc.core.rank.updater;

import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.cache.RankCache;
import me.ponktacology.clashmc.core.rank.factory.RankFactory;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketPlayerAddRank;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketPlayerRemoveRank;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketRemoveRank;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketUpdateRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.Optional;

@Slf4j
public final class RankUpdater implements Updater {

  private final RankFactory rankFactory;
  private final NetworkService networkService;
  private final CorePlayerCache playerCache;

  public RankUpdater(
      RankFactory rankFactory, NetworkService networkService, CorePlayerCache playerCache) {
    this.rankFactory = rankFactory;
    this.networkService = networkService;
    this.playerCache = playerCache;
  }

  public void update( Rank entity) {
    entity.save();
    this.networkService.publish(new PacketUpdateRank(entity.getName()));
  }

  public void remove( Rank entity) {
    this.rankFactory.delete(entity);
    this.networkService.publish(new PacketRemoveRank(entity.getName()));
  }

  public void addRankToPlayer( CorePlayer corePlayer, Grant grant) {
    if (this.playerCache.isOnline(corePlayer)) {
      this.networkService.publish(new PacketPlayerAddRank(corePlayer.getUuid(), grant));
      return;
    }

    corePlayer.addRank(grant);
    corePlayer.save();
  }

  public void removeRankFromPlayer( CorePlayer corePlayer,  Grant grant) {
    if (this.playerCache.isOnline(corePlayer)) {
      this.networkService.publish(new PacketPlayerRemoveRank(corePlayer.getUuid(), grant));
      return;
    }

    corePlayer.removeRank(grant);
    corePlayer.save();
  }

  public static class RankUpdateListener implements PacketListener {

    private final RankFactory rankFactory;
    private final RankCache rankCache;
    private final CorePlayerCache playerCache;

    public RankUpdateListener(
        RankFactory rankFactory, RankCache rankCache, CorePlayerCache playerCache) {
      this.rankFactory = rankFactory;
      this.rankCache = rankCache;
      this.playerCache = playerCache;
    }

    @PacketHandler
    public void onUpdateRank( PacketUpdateRank packet) {
      String rankName = packet.getRankName();

      Optional<Rank> rankOptional = this.rankFactory.load(rankName);

      if (!rankOptional.isPresent()) {
        log.info("Received rank update but rank not found in DataService");
        return;
      }

      this.rankCache.add(rankOptional.get());
    }

    @PacketHandler
    public void onRemoveRank( PacketRemoveRank packet) {
      String rankName = packet.getRankName();

      this.rankCache.remove(rankName);
    }

    @PacketHandler
    public void onPlayerAddRank( PacketPlayerAddRank packet) {
      Player player = Bukkit.getPlayer(packet.getPlayer());

      if (player == null) return;

      Optional<CorePlayer> corePlayerOptional = playerCache.get(player);

      if (!corePlayerOptional.isPresent()) {
        log.info("Player is online but not in cache, player= " + player.getName());
        return;
      }

      CorePlayer corePlayer = corePlayerOptional.get();

      Grant grant = packet.getGrant();
      Optional<Rank> rankOptional = rankCache.get(grant.getRankName());

      if (!rankOptional.isPresent()) {
        log.info(
            "Player is online but rank not in cache, player= "
                + player.getName()
                + ", rank="
                + grant.getRankName());
        return;
      }

      corePlayer.addRank(packet.getGrant());
      corePlayer.save();
    }

    @PacketHandler
    public void onPlayerRemoveRank( PacketPlayerRemoveRank packet) {
      Player player = Bukkit.getPlayer(packet.getPlayer());

      if (player == null) return;

      Optional<CorePlayer> corePlayerOptional = playerCache.get(player);

      if (!corePlayerOptional.isPresent()) {
        log.info("Player is online but not in cache, player= " + player.getName());
        return;
      }

      CorePlayer corePlayer = corePlayerOptional.get();

      Grant grant = packet.getGrant();
      Optional<Rank> rankOptional = rankCache.get(grant.getRankName());

      if (!rankOptional.isPresent()) {
        log.info(
            "Player is online but rank not in cache, player= "
                + player.getName()
                + ", rank="
                + grant.getRankName());
        return;
      }

      corePlayer.removeRank(grant);
    }
  }
}

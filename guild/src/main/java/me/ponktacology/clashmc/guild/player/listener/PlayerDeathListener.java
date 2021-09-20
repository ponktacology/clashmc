package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.death.PlayerRankDeathEvent;
import me.ponktacology.clashmc.core.util.*;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.updater.GuildUpdater;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.DamageCache;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.death.PlayerDeath;
import me.ponktacology.clashmc.guild.player.death.announcer.PlayerDeathAnnouncer;
import me.ponktacology.clashmc.guild.player.region.cache.RegionCache;
import me.ponktacology.clashmc.guild.player.statistics.PlayerStatistics;
import me.ponktacology.clashmc.guild.util.KillingSpreeUtil;
import me.ponktacology.clashmc.guild.util.RankUtil;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.player.SectorPlayer;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.player.info.PlayerInfo;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.player.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class PlayerDeathListener implements Listener {

  private final Location spawnLocation =
      new Location(Bukkit.getWorld("world"), 0, 100, 0, 90.0F, 0F);
  private final GuildPlayerCache playerCache;
  private final PlayerDeathAnnouncer deathAnnouncer;
  private final TaskDispatcher taskDispatcher;
  private final SectorCache sectorCache = SectorPlugin.INSTANCE.getSectorCache();
  private final SectorPlayerFactory sectorPlayerFactory = SectorPlugin.INSTANCE.getPlayerFactory();
  private final PlayerTransferUpdater transferUpdater = SectorPlugin.INSTANCE.getTransferUpdater();
  private final GuildUpdater guildUpdater = GuildPlugin.INSTANCE.getGuildUpdater();

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerDeathEvent(PlayerDeathEvent event) {
    event.setDeathMessage(null);
    Player player = event.getEntity();

    if (player == null) {
      return;
    }

    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(player);

    if (!guildPlayerOptional.isPresent()) {
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    guildPlayer.disableCombatTag();

    PlayerStatistics statistics = guildPlayer.getStatistics();

    DamageCache damageCache = guildPlayer.getDamageCache();
    Optional<UUID> lastAttacker = damageCache.getLastAttacker();
    Player entityKiller = event.getEntity().getKiller();
    Player killer = lastAttacker.isPresent() ? Bukkit.getPlayer(lastAttacker.get()) : entityKiller;

    if (killer == null || killer.equals(player)) {
      onUnknownDeath(guildPlayer);
      return;
    }

    Optional<GuildPlayer> killerGuildPlayerOptional = this.playerCache.get(killer);

    if (!killerGuildPlayerOptional.isPresent()) {
      onUnknownDeath(guildPlayer);
      return;
    }

    GuildPlayer killerGuildPlayer = killerGuildPlayerOptional.get();

    PlayerStatistics killerStatistics = killerGuildPlayer.getStatistics();

    List<ItemStack> drops = event.getDrops();
    InventoryUtil.addItem(killer, drops);

    drops.clear();
    killer.giveExp(event.getDroppedExp());
    event.setDroppedExp(0);

    RankUtil.Type type = RankUtil.Type.NORMAL;

    RegionCache regionCache = killerGuildPlayer.getRegionCache();
    if (regionCache.getLastGuild().isPresent()) {
      type = RankUtil.Type.ON_CUBOID;
    }

    // Player eats in approx. 1.6 seconds
    if (System.currentTimeMillis() - guildPlayer.getLastConsumeTime() < 1600) {
      type = RankUtil.Type.WHILE_EATING;
    }

    if (guildPlayer.hasGuild() && killerGuildPlayer.hasGuild()) {
      if (killerGuildPlayer.getGuild().get().isInWar(guildPlayer.getGuild().get())) {
        type = RankUtil.Type.IN_WAR;
      }
    }

    String killerName = killerGuildPlayer.getName();
    String victimName = guildPlayer.getName();

    boolean hasKilledBefore = killerGuildPlayer.hasKilledBefore(guildPlayer);

    int rankChange =
        hasKilledBefore
            ? 0
            : RankUtil.getChangeBetweenTwoScores(
                killerStatistics.getRank(), statistics.getRank(), type);

    if (hasKilledBefore) {
      killerGuildPlayer.sendMessage(
          Text.colored(
              "&cZabiłeś już tego gracza w przeciągu ostatnich 3 godzin, nie dostaniesz teraz za niego rankingu."));
    } else killerGuildPlayer.addKilledBefore(guildPlayer);

    int killingSpree = killerStatistics.increaseKillingSpree();

    killerGuildPlayer.increaseKills(killerGuildPlayer, guildPlayer, rankChange);
    killerGuildPlayer.increaseRank(rankChange);

    guildPlayer.decreaseRank(rankChange);
    guildPlayer.increaseDeaths(killerGuildPlayer, guildPlayer, rankChange);

    if (killerGuildPlayer.hasGuild()) {
      Guild killerGuild = killerGuildPlayer.getGuild().get();
      int guildRankChange =
          hasKilledBefore
              ? 0
              : RankUtil.getChangeBetweenTwoScores(killerGuild.rank(), statistics.getRank(), type);

      if (guildPlayer.hasGuild()) {
        Guild victimGuild = guildPlayer.getGuild().get();
        guildRankChange =
            hasKilledBefore
                ? 0
                : RankUtil.getChangeBetweenTwoScores(killerGuild.rank(), victimGuild.rank(), type);
        victimGuild.increaseDeaths(killerGuildPlayer, guildPlayer, guildRankChange);

        this.taskDispatcher.runAsync(() -> this.guildUpdater.update(victimGuild));
      }

      killerGuild.increaseKills(killerGuildPlayer, guildPlayer, guildRankChange);
      this.taskDispatcher.runAsync(() -> this.guildUpdater.update(killerGuild));
    } else if (guildPlayer.hasGuild()) {
      Guild victimGuild = guildPlayer.getGuild().get();
      int guildRankChange =
          hasKilledBefore
              ? 0
              : RankUtil.getChangeBetweenTwoScores(
                  killerGuildPlayer.getRank(), victimGuild.rank(), type);
      victimGuild.increaseDeaths(killerGuildPlayer, guildPlayer, guildRankChange);

      this.taskDispatcher.runAsync(() -> this.guildUpdater.update(victimGuild));
    }

    PlayerRankDeathEvent deathEvent = new PlayerRankDeathEvent(player, rankChange);
    PluginUtil.callEvent(deathEvent);

    TitleUtil.sendTitleAndSubtitle(
        killer,
        "&c" + KillingSpreeUtil.convert(killingSpree, type),
        victimName + " &7+" + rankChange,
        5,
        20 * 2,
        5);

    TitleUtil.sendTitleAndSubtitle(
        player,
        type == RankUtil.Type.WHILE_EATING ? "&cPolizałeś!" : "&cZginąłeś!",
        killerName
            + " "
            + StyleUtil.convertHealth(killer.getHealth())
            + " &4"
            + StyleUtil.getHeartIcon()
            + " &7-"
            + rankChange,
        5,
        20 * 2,
        5);

    guildPlayer
        .getHighestDamages()
        .forEach(
            it -> {
              Optional<GuildPlayer> assisterGuildPlayer = this.playerCache.get(it.getKey());

              assisterGuildPlayer.ifPresent(
                  assister -> {
                    PlayerStatistics assisterStatistics = assister.getStatistics();

                    int assisterRankChange = rankChange / 4;
                    assisterStatistics.increaseAssists(assister, guildPlayer, assisterRankChange);
                    assisterStatistics.increaseRank(assisterRankChange);

                    if (assister.isOnThisServer()) {
                      TitleUtil.sendTitleAndSubtitle(
                          assister.getPlayer(),
                          "&cAsystujesz!",
                          victimName + " &6+" + assisterRankChange,
                          5,
                          20 * 2,
                          5);

                      this.taskDispatcher.runAsync(assister::save);
                    }
                  });
            });

    guildPlayer.setDeathLocation(player.getLocation());
    this.onDeath(guildPlayer, player);

    this.taskDispatcher.runAsync(
        () -> {
          this.deathAnnouncer.announce(new PlayerDeath(guildPlayer, killerGuildPlayer, rankChange));
          killerGuildPlayer.save();
        });
  }

  private void onUnknownDeath(GuildPlayer guildPlayer) {
    guildPlayer.decreaseRank(5);

    this.taskDispatcher.runAsync(
        () -> this.deathAnnouncer.announce(new PlayerDeath(guildPlayer, null, 5)));

    Player player = guildPlayer.getPlayer();

    if (player == null) {
      return;
    }

    PlayerRankDeathEvent deathEvent = new PlayerRankDeathEvent(player, 5);
    PluginUtil.callEvent(deathEvent);

    this.onDeath(guildPlayer, player);
  }

  private void onDeath(GuildPlayer guildPlayer, Player player) {
    player.getWorld().strikeLightningEffect(player.getLocation());
    PlayerInfo info = PlayerUtil.wrap(player);
    info.health = 20;
    info.foodLevel = 20;
    info.fireTicks = 1;
    info.fallDistance = 0F;
    info.exhaustion = 0F;
    info.saturation = 12F;
    info.inventory = new Inventory(null, null, info.inventory.getEnderContents());
    info.experience = 0;
    info.fallDistance = 0F;
    info.exhaustion = 0F;
    info.totalExperience = 0;
    info.x = spawnLocation.getBlockX();
    info.y = spawnLocation.getBlockY();
    info.z = spawnLocation.getBlockZ();
    player.spigot().respawn();
    player.kickPlayer(Text.colored("&cZginąłeś"));

    this.taskDispatcher.runLaterAsync(
        () -> {
          guildPlayer.save();
          Optional<Sector> sectorOptional = this.sectorCache.getLeastCrowded(SectorType.SPAWN);

          if (!sectorOptional.isPresent()) {
            log.info("Couldn't find SPAWN server");
            return;
          }

          SectorPlayer sectorPlayer =
              this.sectorPlayerFactory.loadOrCreate(player.getUniqueId(), player.getName());

          this.transferUpdater.transfer(sectorPlayer, sectorOptional.get(), info);
        },
        1L,
        TimeUnit.SECONDS);
  }
}

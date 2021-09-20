package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.PlayerUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.util.GuildRegionUtil;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;
  private final Sector localSector;
  private final SectorCache sectorCache;
  private final PlayerTransferUpdater transferUpdater;
  private final TaskDispatcher taskDispatcher;

  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerInteractEvent( PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Action action = event.getAction();
    ItemStack itemInHand = event.getItem();

    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      if (itemInHand != null && itemInHand.getType().toString().contains("SWORD")) {
        this.taskDispatcher.runLater(
            () -> player.setItemInHand(player.getItemInHand().clone()), 150, TimeUnit.MILLISECONDS);
      }
    }

    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    Block block = event.getClickedBlock();

    if (this.localSector.isSpawn()) {
      if (block.getType() == Material.STONE_BUTTON) {
        Optional<Sector> sectorOptional = this.sectorCache.getLeastCrowded(SectorType.DEFAULT);

        if (!sectorOptional.isPresent()) {
          return;
        }

        Sector sector = sectorOptional.get();

        this.transferUpdater.update(player, sector, RegionUtil.getRandomLocationInSector(sector));
      } else if (block.getType() == Material.WOOD_BUTTON) {
        if (player.getLocation().getBlock().getType() != Material.STONE_PLATE) return;

        Optional<Sector> sectorOptional = this.sectorCache.getLeastCrowded(SectorType.DEFAULT);

        if (!sectorOptional.isPresent()) {
          return;
        }

        Sector sector = sectorOptional.get();

        List<GuildPlayer> entities =
            block.getWorld().getNearbyEntities(block.getLocation(), 3, 3, 3).stream()
                .filter(
                    it ->
                        (it instanceof Player)
                            && it.getLocation().getBlock().getType() == Material.STONE_PLATE)
                .map(it -> (Player) it)
                .map(this.playerCache::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted((o1, o2) -> -(o1.getRank() - o2.getRank()))
                .collect(Collectors.toList());

        if (entities.size() < 2) {
          player.sendMessage(Text.colored("&cBrak wystarczającej ilości graczy do teleportacji."));
          return;
        }

        GuildPlayer guildPlayer1 = entities.get(0);
        GuildPlayer guildPlayer2 = entities.get(1);

        Player player1 = guildPlayer1.getPlayer();
        Player player2 = guildPlayer2.getPlayer();

        PlayerUtil.resetPlayer(player1);
        PlayerUtil.resetPlayer(player2);

        Location location = GuildRegionUtil.getRandomLocationInSector(sector);

        this.transferUpdater.update(player1, sector, location);
        this.transferUpdater.update(player2, sector, location);
      }
    }

    if (!guildPlayer.hasCombatTag()) return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

    if (corePlayer.isStaff()) return;

    switch (block.getType()) {
      case FURNACE:
      case CHEST:
      case ENDER_CHEST:
      case DISPENSER:
      case WORKBENCH:
        player.sendMessage(Text.colored("&cNie możesz korzystać z tego bloku podczas walki."));
        event.setCancelled(true);
        break;
    }
  }
}

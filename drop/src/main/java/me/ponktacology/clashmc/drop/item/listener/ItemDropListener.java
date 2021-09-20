package me.ponktacology.clashmc.drop.item.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.core.util.*;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.player.statistics.DropPlayerStatistics;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ItemDropListener implements Listener {


  private final DropPlayerCache playerCache;

  private final DropItemCache dropItemCache;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockBreakEvent( BlockBreakEvent event) {
    Player player = event.getPlayer();

    if (player.getGameMode() != GameMode.SURVIVAL) {
      return;
    }

    DropPlayer dropPlayer = this.playerCache.getOrKick(player);

    ItemStack itemInHand = player.getItemInHand();
    List<ItemStack> drops = new ArrayList<>(event.getBlock().getDrops(itemInHand));
    Block block = event.getBlock();

    if (event.getBlock().getType() == Material.LEAVES) {
      Optional<DropItem> dropItem = this.dropItemCache.getRandom(dropPlayer, DropType.LEAVES);

      dropItem.ifPresent(it -> InventoryUtil.addItem(player, it.getItem()));
      destroy(event, block, player, drops);
      return;
    }

    DropPlayerStatistics statistics = dropPlayer.getStatistics();

    if (block.getType() != Material.STONE) {
      if (block.getType() == Material.OBSIDIAN) {
        statistics.incrementMinedObsidian();
      }

      drops.removeIf(
          it -> {
            String material = it.getType().toString();

            return material.contains("ORE") || material.contains("COAL");
          });
      destroy(event, block, player, drops);
      return;
    }

    statistics.incrementMinedStone();

    if (!dropPlayer.isMineCobblestone()) {
      drops.removeIf(it -> it.getType().equals(Material.COBBLESTONE));
    }

    Optional<DropItem> dropItemOptional = this.dropItemCache.getRandom(dropPlayer, DropType.STONE);
    event.setExpToDrop(0);

    int exp = 2 + (int) (2 * dropPlayer.getExpMultiplier());

    player.giveExp(exp);
    dropPlayer.incrementPoints();

    if (!dropItemOptional.isPresent()) {
      destroy(event, block, player, drops);
      return;
    }

    DropItem dropItem = dropItemOptional.get();
    boolean nullInHand = itemInHand == null;

    if (nullInHand) {
      destroy(event, block, player, drops);
      return;
    }

    if (!dropPlayer.isItemEnabled(dropItem)
        || dropItem.getDropBelowY() <= block.getLocation().getBlockY()
        || !dropItem.canMine(itemInHand.getType())) {
      destroy(event, block, player, drops);
      return;
    }

    int amount = 1;

    if (dropItem.isFortune() && itemInHand.getType().toString().contains("PICKAXE")) {
      amount =
          ItemUtil.getFortuneLevel(itemInHand) == 0
              ? 1
              : MathUtil.random(1, ItemUtil.getFortuneLevel(itemInHand) + 1);
    }

    drops.add(new ItemBuilder(dropItem.getItem()).amount(amount).build());

    destroy(event, event.getBlock(), player, drops);

    ActionBarUtil.sendActionBarMessage(
        player, Text.colored("&7Znalazłeś " + dropItem.getDisplayName() + " &fx" + amount));
  }

  public void destroy( BlockBreakEvent event,  Block block,  Player player,  List<ItemStack> drops) {
    event.setCancelled(true);
    block.setType(Material.AIR);

    ItemUtil.tryToTakeDurability18(event.getPlayer());

    drops.forEach(it -> InventoryUtil.addItem(player, it));
  }
}

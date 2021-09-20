package me.ponktacology.clashmc.drop.item.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.drop.DropConstants;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


import java.util.Optional;

@RequiredArgsConstructor
public class CobbleXListener implements Listener {

  
  private final DropItemCache dropItemCache;
  
  private final DropPlayerCache playerCache;

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBlockPlaceEvent( BlockPlaceEvent event) {
    ItemStack result = DropConstants.COBBLEX_ITEM;
    Player player = event.getPlayer();
    ItemStack itemInHand = player.getItemInHand();

    if (itemInHand == null || !itemInHand.isSimilar(result)) {
      return;
    }

    DropPlayer dropPlayer = this.playerCache.getOrKick(player);
    Optional<DropItem> dropItemOptional =
        this.dropItemCache.getRandom(dropPlayer, DropType.COBBLEX);

    if (!dropItemOptional.isPresent()) {
      return;
    }

    DropItem dropItem = dropItemOptional.get();

    InventoryUtil.removeItem(player, result);
    InventoryUtil.addItem(player, dropItem.getItem());
    event.setCancelled(true);
    player.sendMessage(
        Text.colored("&aZdobyto " + dropItem.getDisplayName() + "&a z &dCobbleX&a."));
  }
}

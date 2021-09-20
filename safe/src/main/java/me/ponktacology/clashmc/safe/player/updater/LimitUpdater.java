package me.ponktacology.clashmc.safe.player.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.safe.item.SafeItem;
import me.ponktacology.clashmc.safe.item.cache.SafeItemCache;
import me.ponktacology.clashmc.safe.player.SafePlayer;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class LimitUpdater implements Updater {

   private final SafeItemCache itemCache;

  
  public void update( SafePlayer safePlayer) {
    Player player = safePlayer.getPlayer();

    if (player == null) {
      return;
    }

    boolean removed = false;

    for (SafeItem item : this.itemCache.values()) {
      int count = InventoryUtil.countItemsIgnoreItemMeta(player, item.getItem());

      if (count > item.getLimit()) {
        int removeCount = count - item.getLimit();
        InventoryUtil.removeItemIgnoreItemMeta(
            player, new ItemBuilder(item.getItem()).amount(removeCount).build());

        safePlayer.addItems(item, removeCount);
        player.sendMessage(
            Text.colored(
                "&aPrzeniesiono " + removeCount + " " + item.getName() + "&a do twojego schowka."));
        removed = true;
      }
    }

    if (removed) {
      player.updateInventory();
      player.sendMessage(Text.colored("&aMozesz je odebrac pod &f/schowek&a."));
      safePlayer.save();
    }
  }

  @RequiredArgsConstructor
  public static class LimitUpdateTask implements Runnable {

     private final SafePlayerCache playerCache;
     private final LimitUpdater updater;

    @Override
    public void run() {
      for (SafePlayer safePlayer : this.playerCache.values()) {
        this.updater.update(safePlayer);
      }
    }
  }
}

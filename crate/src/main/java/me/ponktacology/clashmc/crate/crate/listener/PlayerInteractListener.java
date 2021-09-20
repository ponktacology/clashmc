package me.ponktacology.clashmc.crate.crate.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.crate.crate.item.CrateItem;
import me.ponktacology.clashmc.crate.crate.opening.CrateOpening;
import me.ponktacology.clashmc.crate.crate.opening.announcer.CrateOpeningAnnouncer;
import me.ponktacology.clashmc.crate.player.CratePlayer;
import me.ponktacology.clashmc.crate.player.cache.CratePlayerCache;
import me.ponktacology.clashmc.crate.player.statistics.CratePlayerStatistics;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PlayerInteractListener implements Listener {

  private final CrateCache crateCache;

  private final CratePlayerCache playerCache;

  private final CrateOpeningAnnouncer openingAnnouncer;

  private final TaskDispatcher taskDispatcher;

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    if (event.getAction() != Action.RIGHT_CLICK_AIR
        && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }

    ItemStack itemStack = event.getItem();

    if (itemStack == null || itemStack.getType() == Material.AIR) {
      return;
    }

    Optional<Crate> crateOptional = this.crateCache.get(itemStack);

    if (!crateOptional.isPresent()) {
      return;
    }

    Crate crate = crateOptional.get();

    if (!crate.isEnabled()) {
      player.sendMessage(Text.colored("&cTa skrzynka jest aktualnie wyłączona."));
      event.setCancelled(true);
      this.taskDispatcher.runLater(player::updateInventory, 1L, TimeUnit.MILLISECONDS);
      return;
    }

    CratePlayer cratePlayer = this.playerCache.getOrKick(player);
    CratePlayerStatistics statistics = cratePlayer.getStatistics();

    if (crate.getName().toUpperCase(Locale.ROOT).contains("PREMIUM")) {
      statistics.incrementOpenedPremiumCrate();
    }

    if (crate.getName().toUpperCase(Locale.ROOT).contains("NORMAL")) {
      statistics.incrementOpenedNormalCrate();
    }

    InventoryUtil.removeItem(player, crate.getCrateItem());

    CrateItem reward = crate.getRandomItem();

    if (reward != null) {
      InventoryUtil.addItem(player, reward.getItem());
      this.openingAnnouncer.announce(new CrateOpening(cratePlayer, crate, reward));
    }

    event.setCancelled(true);
  }
}

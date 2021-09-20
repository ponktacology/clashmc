package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class SpawnProtectionListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    
    if (!block.getType().equals(Material.BOOKSHELF)) {
      return;
    }

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);
    if (!guildPlayer.hasSpawnProtection()) return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);
    if (corePlayer.isStaff()) return;

    player.sendMessage(
        Text.colored(
            "&cNie możesz zniszczyć tego bloku podczas ochrony startowej. Wyłącz ją komendą /ochrona wylacz."));
    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();

    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || item == null) return;
    if (!item.getType().equals(Material.FLINT_AND_STEEL)
        && !item.getType().equals(Material.LAVA_BUCKET)) {
      return;
    }

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);
    if (!guildPlayer.hasSpawnProtection()) return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);
    if (corePlayer.isStaff()) return;

    player.sendMessage(
        Text.colored(
            "&cNie możesz rozlewać lawy ani używać zapalniczki podczas ochrony startowej. Wyłącz ją komendą /ochrona wylacz."));
    event.setCancelled(true);
  }
}

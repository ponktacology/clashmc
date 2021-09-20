package me.ponktacology.clashmc.guild.player.grief.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.grief.PlacedBlock;
import me.ponktacology.clashmc.guild.player.grief.PlacedBlockCache;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@RequiredArgsConstructor
public class AntiGriefListener implements Listener {

  private final PlacedBlockCache blockCache;
  private final GuildCache guildCache;
  private final CorePlayerCache playerCache;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlockPlaced();

    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    if (block.getY() < 60
        || !block.getType().isSolid()
        || player.isSneaking()
        || corePlayer.isStaff()
        || this.guildCache.getByLocation(block.getLocation()).isPresent()) {
      return;
    }

    this.blockCache.add(new PlacedBlock(block));

    player.sendMessage(
        Text.colored(
            "&cTen blok zniknie za 10 minut, aby tego uniknąć, postaw blok przytrzymując shift."));
  }
}

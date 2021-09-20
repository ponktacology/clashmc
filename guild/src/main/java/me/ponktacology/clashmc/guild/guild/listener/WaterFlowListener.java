package me.ponktacology.clashmc.guild.guild.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;


@RequiredArgsConstructor
public class WaterFlowListener implements Listener {

  private final GuildCache guildCache;

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onBlockFromTo( BlockFromToEvent event) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    String type = block.getType().toString();

    if (type.contains("WATER") || type.contains("LAVA")) {
      if (location.getBlockY() < 40) {
        event.setCancelled(!this.guildCache.getByLocation(block.getLocation()).isPresent());
      } else event.setCancelled(true);
    }
  }
}

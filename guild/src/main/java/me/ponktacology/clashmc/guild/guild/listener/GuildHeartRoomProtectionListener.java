package me.ponktacology.clashmc.guild.guild.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

@RequiredArgsConstructor
public class GuildHeartRoomProtectionListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;
  private final GuildCache guildCache;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(BlockBreakEvent event) {
    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    if (!guildPlayer.hasGuild()) return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

    if (corePlayer.isStaff()) return;

    Guild guild = guildPlayer.getGuild().get();

    if (guild.isInHeartRoom(event.getBlock().getLocation())) {
      player.sendMessage(Text.colored("&cNie możesz niszczyć bloków blisko serca gildii."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    if (!guildPlayer.hasGuild()) return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

    if (corePlayer.isStaff()) return;

    Guild guild = guildPlayer.getGuild().get();

    if (guild.isInHeartRoom(event.getBlock().getLocation())) {
      player.sendMessage(Text.colored("&cNie możesz stawiać bloków tak blisko serca gildii."));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(EntityExplodeEvent event) {
    event
        .blockList()
        .removeIf(
            it ->
                it.getType() == Material.EMERALD_BLOCK
                    && this.guildCache.getByCenter(it.getLocation()).isPresent());
  }
}

package me.ponktacology.clashmc.guild.player.region;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class RegionCacheUpdater implements Updater, Listener {

  private final GuildCache guildCache;
  private final GuildPlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;

  public void update(Player player, Location location) {
    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(player);

    if (!guildPlayerOptional.isPresent()) {
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    Optional<Guild> guildOptional = guildPlayer.getRegionCache().getLastGuild();

    if (!guildOptional.isPresent()) {
      guildOptional = this.guildCache.getByLocation(location);

      if (guildOptional.isPresent()) {
        Guild guild = guildOptional.get();
        guildPlayer.getRegionCache().setLastGuild(guild);

        if (guild.allies().stream().anyMatch(it -> it.hasMember(guildPlayer))) {
          guild.broadcast("&8### &9Sojusznik wkroczył na teren twojej gildii &8###");
        } else if (!guild.hasMember(guildPlayer)) {
          guild.broadcast("&8### &cWróg wkroczył na teren twojej gildii &8###");
        }

        player.sendMessage(
            Text.colored(
                guild.hasMember(player)
                    ? "&aWkroczyłeś na teren własnej gildii."
                    : "&cWkroczyłeś na teren gildii " + guild.getTag() + "&c."));
      }
    } else {
      Guild guild = guildOptional.get();

      if (!RegionUtil.isInIgnoreY(location, guild.getRegion())
          || !GuildPlugin.INSTANCE.getGuildCache().get(guild.getTag()).isPresent()) {
        player.sendMessage(
            Text.colored(
                guild.hasMember(player)
                    ? "&aOpuściłeś teren własnej gildii."
                    : "&cOpuściłeś teren gildii " + guild.getTag() + "&c."));
        guildPlayer.getRegionCache().setLastGuild(null);
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerJoinEvent(AsyncPlayerJoinEvent event) {
    this.update(event.getPlayer(), event.getPlayer().getLocation());
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    Location from = event.getFrom();
    Location to = event.getTo();

    if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
      return;
    }

    this.taskDispatcher.runAsync(() -> this.update(event.getPlayer(), to));
  }
}

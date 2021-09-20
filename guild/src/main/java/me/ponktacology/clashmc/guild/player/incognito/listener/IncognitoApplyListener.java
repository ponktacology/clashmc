package me.ponktacology.clashmc.guild.player.incognito.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.incognito.updater.IncognitoUpdater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class IncognitoApplyListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;
  private final IncognitoUpdater incognitoUpdater;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(AsyncPlayerJoinEvent event) {
    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);
    CorePlugin.INSTANCE.getTaskDispatcher().run(() -> this.incognitoUpdater.update(guildPlayer));
  }
}

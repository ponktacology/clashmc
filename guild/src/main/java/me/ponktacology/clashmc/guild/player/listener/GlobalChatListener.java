package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.chat.global.event.GlobalChatEvent;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


@RequiredArgsConstructor
public class GlobalChatListener implements Listener {


  private final GuildPlayerCache playerCache;

  private final CorePlayerCache corePlayerCache;

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onGlobalChatEvent( GlobalChatEvent event) {
    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);
    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

    if (corePlayer.isStaff()) {
      return;
    }

    if (guildPlayer.hasGuild()) {
      event.setFormat("{TAG}" + event.getFormat());
    }

    event.setFormat(
        Text.colored("&8[&7" + guildPlayer.getStatistics().getRank() + "&8] ")
            + event.getFormat());
  }
}

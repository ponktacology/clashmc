package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

@RequiredArgsConstructor
public class PlayerCommandPreProcessListener implements Listener {

  
  private final GuildPlayerCache playerCache;
  
  private final CorePlayerCache corePlayerCache;
  
  private final CombatTagSettingsCache combatTagSettingsCache;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerCommandPreProcessEvent( PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);


    if (guildPlayer.hasCombatTag()) {
      CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

      if (corePlayer.isStaff()) {
        return;
      }

      if (this.combatTagSettingsCache.get().getBlockedCommands().stream()
              .anyMatch(it -> event.getMessage().toLowerCase(Locale.ROOT).startsWith(it))) {
        player.sendMessage(Text.colored("&cNie możesz użyć tej komendy podczas walki."));
        event.setCancelled(true);
      }
    }
  }
}

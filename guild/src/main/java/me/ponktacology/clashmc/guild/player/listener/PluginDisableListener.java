package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import me.ponktacology.clashmc.guild.player.grief.PlacedBlockCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@RequiredArgsConstructor
public class PluginDisableListener implements Listener {

  private final CombatTagSettingsCache combatTagSettingsCache;
  private final GuildPlayerCache playerCache;
  private final GuildCache guildCache;
  private final PlacedBlockCache blockCache;

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPluginDisableEvent(PluginDisableEvent event) {
    if (event.getPlugin().getName().equals(GuildPlugin.INSTANCE.getPlugin().getName())) {
      this.combatTagSettingsCache.get().setEnabled(false);

      for (GuildPlayer guildPlayer : this.playerCache.values()) {
        guildPlayer.disableCombatTag();
        guildPlayer.save();
      }

      for (Guild guild : this.guildCache.values()) {
        guild.despawnHeart();
        guild.save();
      }

      this.blockCache.clearAll();
    }
  }
}

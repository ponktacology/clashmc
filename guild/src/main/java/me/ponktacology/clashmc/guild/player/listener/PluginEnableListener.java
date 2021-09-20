package me.ponktacology.clashmc.guild.player.listener;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

@RequiredArgsConstructor
public class PluginEnableListener implements Listener {

  private final GuildCache guildCache;

  @EventHandler
  public void onPluginEnableEvent(PluginEnableEvent event) {

    if (event.getPlugin().getName().equals("HolographicDisplays")) {
      HologramsAPI.getHolograms(GuildPlugin.INSTANCE.getPlugin()).forEach(it -> it.delete());
      this.guildCache.values().forEach(Guild::spawnHeart);
    }
  }
}

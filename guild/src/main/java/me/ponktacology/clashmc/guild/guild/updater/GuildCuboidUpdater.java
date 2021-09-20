package me.ponktacology.clashmc.guild.guild.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;


import java.util.Optional;

@RequiredArgsConstructor
public class GuildCuboidUpdater implements Updater {


  private final CorePlayerCache corePlayerCache;

  private final GuildPlayerCache playerCache;

  
  public void update( Guild guild) {
    for (GuildPlayer guildPlayer : this.playerCache.values()) {
      Optional<CorePlayer> corePlayerOptional = corePlayerCache.get(guildPlayer);

      if (!corePlayerOptional.isPresent()) continue;

      CorePlayer corePlayer = corePlayerOptional.get();

      if (corePlayer.isStaff()) continue;

      this.update(guild, guildPlayer);
    }
  }

  public void update( Guild guild, GuildPlayer guildPlayer) {
    guild.updateRectangle(guildPlayer);
  }

  public void update(GuildPlayer guildPlayer) {
    for (Guild guild : GuildPlugin.INSTANCE.getGuildCache().values()) {
      this.update(guild, guildPlayer);
    }
  }

  public void remove( Guild guild, GuildPlayer guildPlayer) {
    guild.removeRectangle(guildPlayer);
  }

  public void remove( Guild guild) {
    for (GuildPlayer guildPlayer : this.playerCache.values()) {
      Optional<CorePlayer> corePlayerOptional = corePlayerCache.get(guildPlayer);

      if (!corePlayerOptional.isPresent()) continue;

      CorePlayer corePlayer = corePlayerOptional.get();

      if (corePlayer.isStaff()) continue;

      this.remove(guild, guildPlayer);
    }
  }
}

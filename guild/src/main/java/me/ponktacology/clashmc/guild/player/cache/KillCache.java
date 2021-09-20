package me.ponktacology.clashmc.guild.player.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;


import java.util.UUID;

public class KillCache extends KeyValueCache<UUID, Long> {

  public KillCache() {
    super();
    this.cleanUp();
  }

  public void add( GuildPlayer guildPlayer) {
    super.add(guildPlayer.getUuid(), System.currentTimeMillis());
  }

  public boolean hasKilledBefore( GuildPlayer guildPlayer) {
    return this.hasKilledBefore(guildPlayer.getUuid());
  }

  private boolean hasKilledBefore(UUID uuid) {
    long timeStamp = this.cache().getOrDefault(uuid, 0L);

    return System.currentTimeMillis() - timeStamp < GuildConstants.KILL_CACHE_EVICTION_TIME;
  }

  private void cleanUp() {
    this.cache().keySet().removeIf(it -> !hasKilledBefore(it));
  }
}

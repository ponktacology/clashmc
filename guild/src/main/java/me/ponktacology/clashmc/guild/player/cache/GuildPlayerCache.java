package me.ponktacology.clashmc.guild.player.cache;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.guild.player.GuildPlayer;


import java.util.List;
import java.util.Optional;

public class GuildPlayerCache extends BukkitPlayerCache<GuildPlayer> {

  private final DataService dataService = CorePlugin.INSTANCE.getDataService();

  public GuildPlayerCache(BukkitPlayerFactory<GuildPlayer> playerFactory) {
    super(playerFactory);
  }

  public long getPosition( GuildPlayer guildPlayer) {
    return this.dataService.count(
            GuildPlayer.class,
            Filters.or(
                Filters.gt("statistics.rank", guildPlayer.getRank()),
                Filters.and(
                    Filters.eq("statistics.rank", guildPlayer.getRank()),
                    Filters.gt("statistics.kills", guildPlayer.getKills()))))
        + 1;
  }

  public Optional<GuildPlayer> getTop(int position) {
    List<GuildPlayer> players =
        this.dataService.sorted(
            GuildPlayer.class, Sorts.descending("statistics.rank"), 15);

    if (players.size() <= position) return Optional.empty();

    return Optional.of(players.get(position));
  }
}

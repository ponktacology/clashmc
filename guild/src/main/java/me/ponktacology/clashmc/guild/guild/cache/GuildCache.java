package me.ponktacology.clashmc.guild.guild.cache;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.updater.GuildCuboidUpdater;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GuildCache extends IgnoreCaseKeyValueCache<Guild> {

  private final Map<Location, Guild> guildCenters = Maps.newConcurrentMap();
  private final Set<Guild> guildsOnThisSector = Sets.newConcurrentHashSet();
  private final GuildCuboidUpdater cuboidUpdater;

  public void add(Guild guild) {
    super.add(guild.getTag(), guild);

    if (RegionUtil.isIn(guild.getCenter(), SectorPlugin.INSTANCE.getLocalSector())) {
      this.guildsOnThisSector.add(guild);
      this.guildCenters.put(guild.heartBlockLocation(), guild);
    }

    for (GuildPlayer guildPlayer : GuildPlugin.INSTANCE.getPlayerCache().values()) {
      Optional<Guild> optionalGuild = guildPlayer.getRegionCache().getLastGuild();

      if (optionalGuild.isPresent() && optionalGuild.get().equals(guild)) {
        guildPlayer.getRegionCache().setLastGuild(guild);
      }
    }
  }

  public void addAll(List<Guild> guilds) {
    guilds.forEach(this::add);
  }

  @Override
  public Optional<Guild> remove(String tag) {
    Optional<Guild> guildOptional = super.remove(tag);

    guildOptional.ifPresent(
        it -> {
          it.despawnHeart();
          this.guildsOnThisSector.remove(it);
          this.guildCenters.remove(it.heartBlockLocation());

          for (GuildPlayer guildPlayer : GuildPlugin.INSTANCE.getPlayerCache().values()) {
            Optional<Guild> optionalGuild = guildPlayer.getRegionCache().getLastGuild();

            if (optionalGuild.isPresent() && optionalGuild.get().equals(it)) {
              guildPlayer.getRegionCache().setLastGuild(null);
            }
          }
        });

    return guildOptional;
  }

  public Optional<Guild> getByTag(String tag) {
    return super.get(tag);
  }

  public Optional<Guild> getByName(String name) {
    for (Guild guild : this.values()) {
      if (name.equalsIgnoreCase(guild.getName())) {
        return Optional.of(guild);
      }
    }

    return Optional.empty();
  }

  public Optional<Guild> getByTagOrName(String tagOrName) {
    Optional<Guild> guildOptional = getByTag(tagOrName);

    return guildOptional.isPresent() ? guildOptional : getByName(tagOrName);
  }

  public Optional<Guild> getByCenter(Location center) {
    return Optional.ofNullable(this.guildCenters.get(center));
  }

  public int getPosition(Guild guild) {
    return this.values().stream()
            .sorted(
                Comparator.comparingInt(
                    (o1) -> {
                      if (o1.size() < 5) return 10_000_000 - o1.rank();

                      return -(o1.rank());
                    }))
            .collect(Collectors.toList())
            .indexOf(guild)
        + 1;
  }

  public Optional<Guild> getTop(int position) {
    if (this.size() <= position) return Optional.empty();

    return Optional.of(
        this.values().stream()
            .sorted(
                Comparator.comparingInt(
                    (o1) -> {
                      if (o1.size() < 5) return 10_000_000 - o1.rank();

                      return -(o1.rank());
                    }))
            .collect(Collectors.toList())
            .get(position));
  }

  public Optional<Guild> getByLocation(Location location) {
    return this.guildsOnThisSector.parallelStream()
        .filter(guild -> RegionUtil.isInIgnoreY(location, guild.getRegion()))
        .findFirst();
  }

  public Set<Guild> guildsOnThisSector() {
    return Collections.unmodifiableSet(this.guildsOnThisSector);
  }
}

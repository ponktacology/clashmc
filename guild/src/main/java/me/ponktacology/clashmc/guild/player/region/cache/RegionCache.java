package me.ponktacology.clashmc.guild.player.region.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.guild.guild.Guild;

import java.lang.ref.WeakReference;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
public class RegionCache {

  private WeakReference<Guild> lastGuild;

  public Optional<Guild> getLastGuild() {
    return Optional.ofNullable(this.lastGuild == null ? null : this.lastGuild.get());
  }

  public void setLastGuild(Guild lastGuild) {
    this.lastGuild = new WeakReference<>(lastGuild);
  }
}

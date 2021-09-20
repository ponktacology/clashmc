package me.ponktacology.clashmc.guild.guild.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.guild.guild.Guild;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GuildFactory implements Factory<Guild> {

  private final DataService dataService;

  public Guild create(String tag, String name,  Player creator,  Location center) {
    return new Guild(tag, name, creator, center);
  }

  public Optional<Guild> load(String tag) {
    return this.dataService.load(tag, Guild.class);
  }

  public List<Guild> loadAll() {
    return this.dataService.loadAll(Guild.class);
  }

  public void delete(Guild guild) {
    this.dataService.delete(guild);
  }
}

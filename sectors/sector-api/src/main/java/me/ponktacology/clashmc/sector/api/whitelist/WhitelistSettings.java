package me.ponktacology.clashmc.sector.api.whitelist;

import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SettingsKey(key = "whitelist")
@Entity(database = "sector", collection = "whitelist")
public final class WhitelistSettings extends Settings {

  private final Set<UUID> players = new HashSet<>();
  private String message;
  private boolean enabled;

  public void addPlayer(UUID uuid) {
    this.players.add(uuid);
  }

  public void removePlayer(UUID uuid) {
    this.players.remove(uuid);
  }

  public boolean hasPlayer(UUID uuid) {
    return this.players.contains(uuid);
  }
}

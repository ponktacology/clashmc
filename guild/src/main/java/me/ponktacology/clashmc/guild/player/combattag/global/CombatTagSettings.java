package me.ponktacology.clashmc.guild.player.combattag.global;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(collection = "settings", database = "guild")
@SettingsKey(key = "combattag-settings")
public class CombatTagSettings extends Settings {

  private boolean enabled = true;
  private final Set<String> blockedCommands = new HashSet<>();

  public void addBlockedCommand(String command) {
    this.blockedCommands.add(command);
  }

  public void removeBlockedCommand(String command) {
    this.blockedCommands.remove(command);
  }
}

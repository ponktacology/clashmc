package me.ponktacology.clashmc.queue.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(collection = "settings", database = "queue")
@SettingsKey(key = "queue-settings")
public class QueueSettings extends Settings {
  private boolean enabled = true;
  private boolean advanced = true;
  private int maxPlayers = 2000;
}

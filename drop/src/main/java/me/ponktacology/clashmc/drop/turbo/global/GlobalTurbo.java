package me.ponktacology.clashmc.drop.turbo.global;

import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;
import me.ponktacology.clashmc.drop.turbo.Turbo;

@Entity(collection = "settings", database = "drop")
@SettingsKey(key = "turbo-settings")
public class GlobalTurbo extends Settings {

  private final Turbo turboDrop = new Turbo();
  private final Turbo turboExp = new Turbo();

  public boolean isTurboDropEnabled() {
    return !this.turboDrop.hasExpired();
  }

  public boolean isTurboExpEnabled() {
    return !this.turboExp.hasExpired();
  }

  public void setTurboDropDuration(long duration) {
    this.turboDrop.setDuration(duration);
  }

  public void setTurboExpDuration(long duration) {
    this.turboExp.setDuration(duration);
  }
}

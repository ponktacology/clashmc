package me.ponktacology.clashmc.sector.api.motd;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;


@Getter
@Setter
@SettingsKey(key = "motd")
@Entity(database = "sector", collection = "motd")
public final class MotdSettings extends Settings {

  private String first, second;

  
  public String getFormattedMotd() {
    StringBuilder formatted = new StringBuilder();

    if (!Strings.isNullOrEmpty(this.first)) {
      formatted.append(this.first).append('\n');
    }

    if (!Strings.isNullOrEmpty(this.second)) {
      formatted.append(this.second);
    }

    return formatted.toString();
  }
}

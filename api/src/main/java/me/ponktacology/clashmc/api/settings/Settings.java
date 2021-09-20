package me.ponktacology.clashmc.api.settings;

import com.google.gson.annotations.SerializedName;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.util.SettingsUtil;

public abstract class Settings {

  @SerializedName("_id")
  private final String key;

  public Settings() {
    this.key = SettingsUtil.getKey(this.getClass());
  }

  public void save( DataService dataService) {
    dataService.save(this);
  }
}

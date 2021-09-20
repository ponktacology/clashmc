package me.ponktacology.clashmc.api.settings.factory;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.util.SettingsUtil;

import java.util.Optional;

@Slf4j
public abstract class SettingsFactory<V extends Settings> implements Factory<V> {

  private final DataService dataService;

  private final Class<V> settingsType;
  private final String key;

  public SettingsFactory(DataService dataService,  Class<V> settingsType) {
    this.dataService = dataService;
    this.settingsType = settingsType;
    this.key = SettingsUtil.getKey(settingsType);
  }

  @SneakyThrows
  public V loadOrCreate() {
    Optional<V> vOptional = this.dataService.load(key, settingsType);

    return vOptional.orElse(settingsType.newInstance());
  }
}

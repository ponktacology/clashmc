package me.ponktacology.clashmc.api.settings.cache;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.cache.reference.ReferenceCache;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.factory.SettingsFactory;


@RequiredArgsConstructor
public class SettingsCache<V extends Settings> extends ReferenceCache<V> {

  private final SettingsFactory<V> settingsFactory;

  public V get() {
    V value = super.get();

    if (value == null) {
      value = this.settingsFactory.loadOrCreate();

      super.set(value);
    }

    return value;
  }
}

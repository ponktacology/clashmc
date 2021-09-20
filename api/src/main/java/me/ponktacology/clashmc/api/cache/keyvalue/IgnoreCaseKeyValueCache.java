package me.ponktacology.clashmc.api.cache.keyvalue;

import java.util.Locale;
import java.util.Optional;

public class IgnoreCaseKeyValueCache<V> extends KeyValueCache<String, V> {

  @Override
  public void add(String key, V value) {
    super.add(key.toUpperCase(Locale.ROOT), value);
  }

  @Override
  public Optional<V> remove(String key) {
    return super.remove(key.toUpperCase(Locale.ROOT));
  }

  @Override
  public Optional<V> get(String key) {
    return super.get(key.toUpperCase(Locale.ROOT));
  }
}

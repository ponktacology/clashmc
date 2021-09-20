package me.ponktacology.clashmc.api.cache.keyvalue;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.api.cache.Cache;

import java.util.*;

public class KeyValueCache<K, V> implements Cache<V> {

  private final Map<K, V> cache;

  public KeyValueCache(boolean concurrent) {
    this.cache = concurrent ? Maps.newConcurrentMap() : Maps.newHashMap();
  }

  public KeyValueCache() {
    this(true);
  }

  public void add(K key, V value) {
    this.cache.put(key, value);
  }

  public void addAll(Map<K, V> values) {
    this.cache.putAll(values);
  }

  public Optional<V> remove(K key) {
    return Optional.ofNullable(this.cache.remove(key));
  }

  public Optional<V> get(K key) {
    return Optional.ofNullable(this.cache.get(key));
  }

  public boolean contains(K key) {
    return this.cache.containsKey(key);
  }

  public Set<K> keys() {
    return Collections.unmodifiableSet(this.cache.keySet());
  }

  public Set<V> values() {
    return Collections.unmodifiableSet(new HashSet<>(this.cache.values()));
  }

  public int size() {
    return this.cache.size();
  }

  protected Map<K, V> cache() {
    return this.cache;
  }
}

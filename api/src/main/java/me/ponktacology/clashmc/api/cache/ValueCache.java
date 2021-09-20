package me.ponktacology.clashmc.api.cache;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

public class ValueCache<V> implements Cache<V> {


  private final Set<V> cache;

  public ValueCache(boolean concurrent) {
    this.cache = concurrent ? Sets.newConcurrentHashSet() : Sets.newHashSet();
  }

  public ValueCache() {
    this(true);
  }

  public void add(V value) {
    this.cache.add(value);
  }

  public void remove(V value) {
    this.cache.remove(value);
  }

  public void removeIf(Predicate<V> predicate) {
    this.cache.removeIf(predicate);
  }

  public void addAll( Collection<V> values) {
    values.forEach(this::add);
  }


  public Set<V> values() {
    return Collections.unmodifiableSet(this.cache);
  }


  public Set<V> cache() {
    return this.cache;
  }
}

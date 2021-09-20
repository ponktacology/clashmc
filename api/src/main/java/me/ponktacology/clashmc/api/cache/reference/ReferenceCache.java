package me.ponktacology.clashmc.api.cache.reference;

import com.google.common.collect.Sets;
import me.ponktacology.clashmc.api.cache.Cache;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ReferenceCache<V> implements Cache<V> {

  private final AtomicReference<V> reference = new AtomicReference<>();

  public void set(V value) {
    this.reference.set(value);
  }

  public Optional<V> remove() {
    return Optional.ofNullable(this.reference.getAndSet(null));
  }

  public V get() {
    return this.reference.get();
  }


  public Set<V> values() {
    return Collections.unmodifiableSet(Sets.newHashSet(reference.get()));
  }
}

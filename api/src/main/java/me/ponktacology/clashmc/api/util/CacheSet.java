package me.ponktacology.clashmc.api.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CacheSet<V> implements Set<V> {

  private final Cache<V, Boolean> cache;

  protected CacheSet(Cache<V, Boolean> cache) {
    this.cache = cache;
  }


  public static <V> Builder<V> newBuilder() {
    return new Builder<>();
  }

  @Override
  public int size() {
    return (int) cache.estimatedSize();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean contains( Object o) {
    return cache.getIfPresent(o) != null;
  }


  @Override
  public Iterator<V> iterator() {
    return cache.asMap().keySet().iterator();
  }


  @Override
  public Object[] toArray() {
    return cache.asMap().keySet().toArray();
  }

  @Override
  public boolean add( V o) {
    boolean contains = contains(o);
    cache.put(o, Boolean.TRUE);
    return contains;
  }

  @Override
  public boolean remove( Object o) {
    boolean contains = contains(o);
    cache.invalidate(o);

    return contains;
  }

  @Override
  public boolean addAll( Collection c) {
    int sizeBefore = size();
    for (Object element : c) {
      cache.put((V) element, Boolean.TRUE);
    }
    return size() != sizeBefore;
  }

  @Override
  public void clear() {
    cache.invalidateAll();
  }

  @Override
  public boolean removeAll( Collection c) {
    int sizeBefore = size();
    for (Object element : c) {
      cache.invalidate(element);
    }
    return size() != sizeBefore;
  }

  @Override
  public boolean retainAll( Collection c) {
    int sizeBefore = size();
    for (Object element : cache.asMap().keySet()) {
      if (!c.contains(element)) {
        remove(element);
      }
    }
    return size() != sizeBefore;
  }

  @Override
  public boolean containsAll(Collection c) {
    for (Object element : cache.asMap().keySet()) {
      if (!contains(element)) return false;
    }
    return true;
  }


  @Override
  public Object[] toArray(Object[] a) {
    return this.toArray();
  }

  public static final class Builder<V> {


    private final Caffeine<Object, Object> caffeine;

    public Builder() {
      this.caffeine = Caffeine.newBuilder();
    }


    public Builder<V> expireAfterWrite(long duration,  TimeUnit unit) {
      this.caffeine.expireAfterWrite(duration, unit);
      return this;
    }


    public Builder<V> weakKeys() {
      this.caffeine.weakKeys();

      return this;
    }


    public CacheSet<V> build() {
      return new CacheSet<>(this.caffeine.build());
    }
  }
}

package me.ponktacology.clashmc.api.configuration;

public interface Configuration {

  <V> V get(String path);

  <V> void set(String path, V entity);

  default void save() {}

  default void load() {}
}

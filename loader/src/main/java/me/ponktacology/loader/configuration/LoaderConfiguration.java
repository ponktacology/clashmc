package me.ponktacology.loader.configuration;

import me.ponktacology.clashmc.api.configuration.GsonConfiguration;
import me.ponktacology.clashmc.api.serializer.Serializer;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LoaderConfiguration extends GsonConfiguration {

  public LoaderConfiguration(Serializer serializer) {
    super("Loader/config", serializer);
  }


  @Override
  public Map<String, Object> getDefaults() {
    Map<String, Object> defaults = new TreeMap<>();

    defaults.put("key", "P8mFLvHrKJH22Y5k");
    defaults.put("plugins", Arrays.asList("core", "sector", "guild"));

    return defaults;
  }


  public String getKey() {
    return get("key");
  }


  public List<String> getPlugins() {
    return get("plugins");
  }
}

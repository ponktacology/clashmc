package me.ponktacology.clashmc.sector.proxy.configuration;

import me.ponktacology.clashmc.api.configuration.GsonConfiguration;
import me.ponktacology.clashmc.api.serializer.Serializer;

import java.util.Map;
import java.util.TreeMap;

public class SectorConfiguration extends GsonConfiguration {

  public SectorConfiguration(Serializer serializer) {
    super("Proxy/config", serializer);
  }

  @Override
  public Map<String, Object> getDefaults() {
    Map<String, Object> defaults = new TreeMap<>();

    defaults.put("redis-url", "");
    defaults.put("redis-password", "");
    defaults.put("mongo-url", "");
    defaults.put("proxy-name", "");
    defaults.put("test-mode", false);
    defaults.put("main", false);

    return defaults;
  }

  public boolean isMain() {
    return get("main");
  }

  public String getProxyName() {
    return get("proxy-name");
  }

  public String getMongoUrl() {
    return get("mongo-url");
  }

  public String getRedisUrl() {
    return get("redis-url");
  }

  public String getRedisPassword() {
    return get("redis-password");
  }

  public boolean getTestMode() {
    return get("test-mode");
  }
}

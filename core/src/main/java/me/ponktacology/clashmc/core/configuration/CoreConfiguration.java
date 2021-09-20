package me.ponktacology.clashmc.core.configuration;

import me.ponktacology.clashmc.api.configuration.GsonConfiguration;
import me.ponktacology.clashmc.api.serializer.Serializer;


import java.util.Map;
import java.util.TreeMap;

public class CoreConfiguration extends GsonConfiguration {

  public CoreConfiguration(Serializer serializer) {
    super("Core/config", serializer);
  }

  
  @Override
  public Map<String, Object> getDefaults() {
    Map<String, Object> defaults = new TreeMap<>();

    defaults.put("use-local-chat-announcer", false);
    defaults.put("use-default-global-chat-announcer", false);
    defaults.put("redis-uri", "");
    defaults.put("redis-password", "");
    defaults.put("redis-cache-uri", "");
    defaults.put("redis-cache-password", "");
    defaults.put("mongo-url", "");
    defaults.put("server-name", "spawn");

    return defaults;
  }

  
  public String getServerName() {
    return get("server-name");
  }
  
  public String getMongoUrl() {
    return get("mongo-url");
  }
  
  public String getRedisUrl() {
    return get("redis-uri");
  }
  
  public String getRedisPassword() {
    return get("redis-password");
  }

  public String getRedisCacheUrl() {
    return get("redis-cache-uri");
  }

  public String getRedisCachePassword() {
    return get("redis-cache-password");
  }

  public boolean useLocalChatAnnouncer() {
    return get("use-local-chat-announcer");
  }

  public boolean useDefaultGlobalChatAnnouncer() {
    return get("use-default-global-chat-announcer");
  }
}

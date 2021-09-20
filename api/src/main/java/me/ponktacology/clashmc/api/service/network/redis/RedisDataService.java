package me.ponktacology.clashmc.api.service.network.redis;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.serializer.Serializer;
import me.ponktacology.clashmc.api.serializer.exception.SerializationException;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.service.network.packet.listener.cache.PacketListenerCache;
import me.ponktacology.clashmc.api.service.network.packet.listener.wrapper.PacketListenerWrapper;
import org.bson.Document;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class RedisDataService implements DataService, NetworkService {

  private final Map<String, RLocalCachedMap<String, String>> caches = Maps.newConcurrentMap();

  private final RedissonClient redissonClient;
  private final PacketListenerCache packetListenerCache;
  private final PacketCache packetCache;
  private final Serializer serializer;
  private final MessageListener<String> jedisPubSub =
      new MessageListener<String>() {
        @Override
        public void onMessage(CharSequence charSequence, String s) {
          String channel = charSequence.toString();
          Optional<Class<? extends Packet>> packetTypeOptional = packetCache.get(channel);

          if (!packetTypeOptional.isPresent()) {
            log.info("Couldn't find correct channel for packet= " + s);
            return;
          }

          Packet packet = null;

          try {
            packet = serializer.deserialize(s, packetTypeOptional.get());
          } catch (Exception e) {
            log.info(
                "Couldn't deserialize entity type= "
                    + packetTypeOptional.get().getSimpleName()
                    + ", json= "
                    + s);
            e.printStackTrace();
          }

          if (packet == null) {
            return;
          }

          packetListenerCache.call(packet);
        }
      };
  @Setter @Getter private boolean enableCache = true;

  public RedisDataService(
      String connectionUrl,
      String password,
      PacketListenerCache packetListenerCache,
      PacketCache packetCache,
      Serializer serializer) {
    this.packetListenerCache = packetListenerCache;
    this.packetCache = packetCache;
    this.serializer = serializer;

    Config config = new Config();

    SingleServerConfig singleServerConfig = config.useSingleServer();

    singleServerConfig.setAddress(connectionUrl);
    singleServerConfig.setPassword(password);

    config.setUseScriptCache(true);
    config.setKeepPubSubOrder(true);

    this.redissonClient = Redisson.create(config);
  }

  @Override
  public <V, T> Optional<V> load(T key, Class<V> type) {
    String channel = getChannel(type);

    RMap<String, String> rMap = this.getCache(channel);

    String json = rMap.get(key.toString());

    if (json == null) {
      return Optional.empty();
    }

    V entity = null;
    try {
      entity = this.serializer.deserialize(json, type);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(entity);
  }

  @Override
  public <V> void save(V entity) {
    String json = null;

    try {
      json = this.serializer.serialize(entity);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    if (json == null) return;

    Document document = Document.parse(json);

    if (document == null) return;

    String channel = getChannel(entity.getClass());

    Object key = document.get("_id");

    this.getCache(channel).fastPut(key.toString(), document.toJson());

    log.debug("Successfully replaced entity, key= " + key + ", document= " + document);
  }

  @Override
  public <V> void delete(V entity) {
    String channel = getChannel(entity.getClass());

    Optional<Object> keyOptional = getKey(entity);

    if (!keyOptional.isPresent()) {
      log.info("There is no field with name _id or SerializedName _id");
      return;
    }

    Object key = keyOptional.get();

    this.getCache(channel).fastRemove(key.toString());

    log.debug("Successfully deleted entity, key= " + key);
  }

  @Override
  public void shutdown() {
    this.redissonClient.shutdown();
  }

  @Override
  public void publish(Packet packet) {
    String json = null;
    try {
      json = this.serializer.serialize(packet);
    } catch (Exception e) {
      log.info("Couldn't serialize entity type= " + packet.getClass().getSimpleName());
      e.printStackTrace();
    }

    if (json == null) {
      return;
    }

    Optional<String> channelOptional = this.packetCache.get(packet.getClass());

    if (!channelOptional.isPresent()) {
      log.info("Couldn't find correct channel for packet= " + packet.getClass().getSimpleName());
      return;
    }

    RTopic topic = this.redissonClient.getTopic(channelOptional.get());
    topic.publish(json);
  }

  @Override
  public void subscribe(PacketListener listener) {
    this.packetListenerCache.add(listener);

    Optional<PacketListenerWrapper> containerOptional = this.packetListenerCache.get(listener);

    if (!containerOptional.isPresent()) {
      log.info(
          "Couldn't find PacketListenerContainer for this listener type= " + listener.getClass());
      return;
    }

    String[] channels =
        containerOptional.get().getPackets().stream()
            .filter(
                it -> {
                  return it.isAnnotationPresent(PacketManifest.class);
                })
            .map(it -> it.getAnnotation(PacketManifest.class).channel())
            .distinct()
            .toArray(String[]::new);

    for (String channel : channels) {
      this.redissonClient.getTopic(channel).addListener(String.class, jedisPubSub);
    }
  }

  @Override
  public void unsubscribe(PacketListener listener) {
    Optional<PacketListenerWrapper> containerOptional = this.packetListenerCache.get(listener);

    if (!containerOptional.isPresent()) {
      log.info(
          "Couldn't find PacketListenerContainer for this listener type= " + listener.getClass());
      return;
    }

    String[] channels =
        containerOptional.get().getPackets().stream()
            .map(it -> it.getAnnotation(PacketManifest.class).channel())
            .distinct()
            .toArray(String[]::new);

    for (String channel : channels) {
      this.redissonClient.getTopic(channel).removeListener(jedisPubSub);
    }

    this.packetListenerCache.add(listener);
  }

  @Override
  public void del(String channel, String key) {
    RMap<String, String> map = this.getCache(channel);
    map.fastRemove(key);
  }

  @Override
  public void set(String channel, String key, String value) {
    RMap<String, String> map = this.getCache(channel);
    map.fastPut(key, value);
  }

  @Override
  public <V> void set(String channel, String key, V value) {
    String json = null;
    try {
      json = this.serializer.serialize(value);
    } catch (Exception e) {
      log.info("Couldn't serialize entity type= " + value.getClass().getSimpleName());
      e.printStackTrace();
    }

    if (json == null) return;

    this.set(channel, key, json);
  }

  @Override
  public <V> Optional<V> get(String channel, String key, Class<V> type) {
    Optional<String> jsonOptional = this.get(channel, key);

    if (!jsonOptional.isPresent()) return Optional.empty();

    V entity = null;
    try {
      entity = this.serializer.deserialize(jsonOptional.get(), type);
    } catch (SerializationException e) {
      e.printStackTrace();
    }

    return Optional.ofNullable(entity);
  }

  @Override
  public Optional<String> get(String channel, String key) {
    RMap<String, String> map = this.getCache(channel);

    return Optional.ofNullable(map.get(key));
  }

  private String getChannel(Class<?> clazz) {
    String collection =
        collections.computeIfAbsent(
            clazz,
            c ->
                clazz.isAnnotationPresent(Entity.class)
                    ? clazz.getAnnotation(Entity.class).collection()
                    : clazz.getSimpleName());
    String database =
        databases.computeIfAbsent(
            clazz,
            c ->
                clazz.isAnnotationPresent(Entity.class)
                    ? clazz.getAnnotation(Entity.class).database()
                    : clazz.getSimpleName());

    return collection + "::" + database;
  }

  public RMap<String, String> getCache(String channel) {
    if (!this.enableCache) {
      return this.redissonClient.getMap(channel);
    }

    return this.caches.computeIfAbsent(
        channel,
        c ->
            this.redissonClient.getLocalCachedMap(
                c,
                LocalCachedMapOptions.<String, String>defaults()
                    .cacheSize(5000)
                    .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.SOFT)
                    .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR)
                    .syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
                    .cacheProvider(LocalCachedMapOptions.CacheProvider.CAFFEINE)));
  }
}

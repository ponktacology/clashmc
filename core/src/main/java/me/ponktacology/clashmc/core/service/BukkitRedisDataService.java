package me.ponktacology.clashmc.core.service;

import me.ponktacology.clashmc.api.serializer.Serializer;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.api.service.network.packet.listener.cache.PacketListenerCache;
import me.ponktacology.clashmc.api.service.network.redis.RedisDataService;
import me.ponktacology.clashmc.core.service.exception.SyncDataManageException;
import org.bukkit.Bukkit;

import java.util.Optional;

public class BukkitRedisDataService extends RedisDataService {

  public BukkitRedisDataService(
      String connectionUrl,
      String password,
      PacketListenerCache packetListenerCache,
      PacketCache packetCache,
      Serializer serializer) {
    super(connectionUrl, password, packetListenerCache, packetCache, serializer);
  }

  @Override
  public <V, T> Optional<V> load(T key, Class<V> type) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Loading data from redis on main thread").printStackTrace();
    }

    return super.load(key, type);
  }

  @Override
  public <V> void save(V entity) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Saving data from redis on main thread").printStackTrace();
    }

    super.save(entity);
  }

  @Override
  public <V> void delete(V entity) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Deleting data from redis on main thread").printStackTrace();
    }

    super.delete(entity);
  }

  @Override
  public void publish(Packet packet) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Publishing packet to redis on main thread").printStackTrace();
    }

    super.publish(packet);
  }

  @Override
  public void del(String channel, String key) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Deleting data from redis on main thread").printStackTrace();
    }

    super.del(channel, key);
  }

  @Override
  public void set(String channel, String key, String value) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Setting data from redis on main thread").printStackTrace();
    }

    super.set(channel, key, value);
  }

  @Override
  public Optional<String> get(String channel, String key) {
    if (Bukkit.isPrimaryThread()) {
      new SyncDataManageException("Getting data from redis on main thread").printStackTrace();
    }

    return super.get(channel, key);
  }
}

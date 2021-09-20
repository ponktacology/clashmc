package me.ponktacology.clashmc.api.service.network;

import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;

import java.util.Optional;

public interface NetworkService {

  /*
   Publishes packet to every @packet listener connected to the same instance
  */
  void publish(Packet packet);

  /*
   Subscribes to specific @listener PacketListener
  */
  void subscribe(PacketListener listener);

  void unsubscribe(PacketListener listener);

  void del(String channel, String key);

  void set(String channel, String key, String value);

  <V> void set(String channel, String key, V value);

  <V> Optional<V> get(String channel, String key, Class<V> type);

  Optional<String> get(String channel, String key);
}

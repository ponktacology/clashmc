package me.ponktacology.clashmc.api.service.network.packet.cache;

import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;

import java.util.Optional;

@Slf4j
public class PacketCache extends KeyValueCache<String, Class<? extends Packet>> {

  public void add( Class<? extends Packet> packet) {
    if (!packet.isAnnotationPresent(PacketManifest.class)) {
      log.info("PacketManifest annotation is missing, packet= " + packet.getSimpleName());
      return;
    }

    PacketManifest manifest = packet.getAnnotation(PacketManifest.class);

    super.add(manifest.channel(), packet);
  }

  
  public Optional<String> get( Class<? extends Packet> packet) {
    return Optional.of(packet.getAnnotation(PacketManifest.class).channel());
  }
}

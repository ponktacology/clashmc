package me.ponktacology.clashmc.api.service.network.packet.listener.cache;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.service.network.packet.listener.wrapper.PacketListenerWrapper;


import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PacketListenerCache extends KeyValueCache<PacketListener, PacketListenerWrapper> {

   private final TaskDispatcher taskDispatcher;

  public void add( PacketListener listener) {
    this.add(listener, new PacketListenerWrapper(listener));
  }

  public void call( Packet packet) {
    for (PacketListenerWrapper container :
        this.values().stream()
            .filter(it -> it.getPackets().contains(packet.getClass()))
            .collect(Collectors.toSet())) {
      this.taskDispatcher.runAsync(() -> container.handle(packet));
    }
  }
}

package me.ponktacology.clashmc.api.announcer;

import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;

public interface Announcer<V, K extends PacketAnnounce> extends PacketListener {

  /*
   Announces @announce object to all instances
  */
  default void announce(V announce) {
    throw new UnsupportedOperationException("unsupported");
  }

  /*
   Handles announced @announce object from all instances
  */
  default void handle(K packet) {
    throw new UnsupportedOperationException("unsupported");
  }
}

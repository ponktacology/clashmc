package me.ponktacology.clashmc.core.chat.global.broadcast.announcer.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.core.chat.global.broadcast.type.BroadcastType;


@PacketManifest(channel = "packet-broadcast-announce")
@Data
public class PacketBroadcastAnnounce implements PacketAnnounce {
  
  private final String message;
  
  private final BroadcastType type;
  private final int fadeIn, duration, fadeOut;
}

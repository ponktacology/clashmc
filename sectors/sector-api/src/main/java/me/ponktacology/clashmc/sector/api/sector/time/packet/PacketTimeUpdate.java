package me.ponktacology.clashmc.sector.api.sector.time.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;

@Getter
@RequiredArgsConstructor
@PacketManifest(channel = "packet-time-update")
public class PacketTimeUpdate implements PacketUpdate<Long> {
  private final long time;
}

package me.ponktacology.clashmc.sector.api.player.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "time-sync")
public class PacketTimeSync implements Packet {

  private final long time;
}

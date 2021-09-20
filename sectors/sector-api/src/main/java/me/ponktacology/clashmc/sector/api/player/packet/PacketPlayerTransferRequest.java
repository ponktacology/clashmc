package me.ponktacology.clashmc.sector.api.player.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.Packet;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "player-transfer-request")
public class PacketPlayerTransferRequest implements Packet {

  private final UUID uuid;

  private final String to;
}

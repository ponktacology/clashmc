package me.ponktacology.clashmc.core.player.privatemessage.announcer.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-private-message-announce")
public class PacketPrivateMessageAnnounce implements PacketAnnounce {

  private final UUID sender;

  private final UUID receiver;

  private final String message;
}

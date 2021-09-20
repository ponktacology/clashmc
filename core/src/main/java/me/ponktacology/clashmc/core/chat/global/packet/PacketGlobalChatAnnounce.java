package me.ponktacology.clashmc.core.chat.global.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "global-chat-announce")
public class PacketGlobalChatAnnounce implements PacketAnnounce {

  private final UUID sender;

  private final String format;

  private final String message;
}

package me.ponktacology.clashmc.core.chat.staff.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "staff-chat-announce")
public class PacketStaffChatAnnounce implements PacketAnnounce {

  private final UUID sender;

  private final String message;
}

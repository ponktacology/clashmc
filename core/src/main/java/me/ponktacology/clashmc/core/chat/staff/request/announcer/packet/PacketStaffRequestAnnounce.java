package me.ponktacology.clashmc.core.chat.staff.request.announcer.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-staff-request-announce")
public class PacketStaffRequestAnnounce implements PacketAnnounce {

  private final UUID sender;

  private final String message;
}

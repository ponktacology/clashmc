package me.ponktacology.clashmc.core.punishment.anouncer.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.core.punishment.Punishment;


@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "punishment-announce")
public class PacketPunishmentAnnounce implements PacketAnnounce {


  private final String formattedIssuer;

  private final String formattedReceiver;

  private final Punishment punishment;
}

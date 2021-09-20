package me.ponktacology.clashmc.crate.crate.opening.announcer.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


@Data
@PacketManifest(channel = "packet-crate-opening-announce")
public class PacketCrateOpeningAnnounce implements PacketAnnounce {
  
  private final String message;
}

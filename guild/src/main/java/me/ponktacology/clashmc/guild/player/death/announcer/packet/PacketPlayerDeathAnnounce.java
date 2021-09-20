package me.ponktacology.clashmc.guild.player.death.announcer.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-death-announce")
public class PacketPlayerDeathAnnounce implements PacketAnnounce {


  private final UUID player;

  private final UUID killer;
  private final int rank;
}

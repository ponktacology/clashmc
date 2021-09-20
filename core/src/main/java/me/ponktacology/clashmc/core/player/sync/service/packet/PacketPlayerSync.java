package me.ponktacology.clashmc.core.player.sync.service.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.core.player.sync.PlayerSync;

import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-sync")
public class PacketPlayerSync implements PacketUpdate<PlayerSync> {

  private final UUID player;
  private final PlayerSync sync;
}

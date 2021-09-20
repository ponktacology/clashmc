package me.ponktacology.clashmc.drop.turbo.player.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.drop.turbo.player.PlayerTurbo;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-turbo-update")
public class PacketPlayerTurboUpdate implements PacketUpdate<PlayerTurbo> {

  private final UUID player;
  private final boolean isTurboDrop;
  private final long duration;
}

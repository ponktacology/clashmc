package me.ponktacology.clashmc.sector.api.redirect;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-redirect-update")
public class PacketPlayerRedirectUpdate implements PacketUpdate<UUID> {

  private final UUID uuid;
}

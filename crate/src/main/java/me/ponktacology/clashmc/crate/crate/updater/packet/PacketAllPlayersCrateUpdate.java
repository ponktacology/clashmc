package me.ponktacology.clashmc.crate.crate.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.crate.player.CratePlayer;


@Data
@PacketManifest(channel = "packet-all-players-crate-update")
public class PacketAllPlayersCrateUpdate implements PacketUpdate<CratePlayer> {

    private final String crate;
    private final int amount;
}

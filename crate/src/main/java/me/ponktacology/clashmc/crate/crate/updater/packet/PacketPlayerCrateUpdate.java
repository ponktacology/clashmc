package me.ponktacology.clashmc.crate.crate.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.crate.player.CratePlayer;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-crate-player-crate-update")
public class PacketPlayerCrateUpdate implements PacketUpdate<CratePlayer> {

    private final UUID player;

    private final String crate;
    private final int amount;
}

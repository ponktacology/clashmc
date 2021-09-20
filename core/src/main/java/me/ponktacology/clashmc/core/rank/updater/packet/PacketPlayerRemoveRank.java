package me.ponktacology.clashmc.core.rank.updater.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.grant.Grant;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-player-remove-rank")
public class PacketPlayerRemoveRank implements PacketUpdate<Rank> {

    private final UUID player;

    private final Grant grant;
}

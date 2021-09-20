package me.ponktacology.clashmc.core.rank.updater.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.core.rank.Rank;


@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-update-rank")
public final class PacketUpdateRank implements PacketUpdate<Rank> {


  private final String rankName;
}

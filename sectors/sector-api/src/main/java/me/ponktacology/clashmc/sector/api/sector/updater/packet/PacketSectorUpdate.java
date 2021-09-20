package me.ponktacology.clashmc.sector.api.sector.updater.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.api.sector.Sector;


@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "sector-update")
public class PacketSectorUpdate implements PacketUpdate<Sector> {


  private final String sector;
}

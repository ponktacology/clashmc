package me.ponktacology.clashmc.crate.crate.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.crate.crate.Crate;


@Data
@PacketManifest(channel = "packet-crate-remove")
public class PacketCrateRemove implements PacketUpdate<Crate> {

  private final String name;
}

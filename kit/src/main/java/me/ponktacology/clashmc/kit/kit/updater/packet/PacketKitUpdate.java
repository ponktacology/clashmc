package me.ponktacology.clashmc.kit.kit.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.kit.kit.Kit;


@Data
@PacketManifest(channel = "packet-kit-update")
public class PacketKitUpdate implements PacketUpdate<Kit> {

    private final String name;
}

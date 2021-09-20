package me.ponktacology.clashmc.sector.player.inventory.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.player.inventory.PlayerInventoryUpdate;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-inventory-request")
public class PacketPlayerInventoryRequest implements PacketUpdate<PlayerInventoryUpdate> {

  private final UUID player;
}

package me.ponktacology.clashmc.sector.player.inventory.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;
import me.ponktacology.clashmc.sector.player.inventory.PlayerInventoryUpdate;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-inventory-update")
public class PacketPlayerInventoryUpdate implements PacketUpdate<PlayerInventoryUpdate> {

  private final UUID player;

  private final InventoryUpdate update;
}

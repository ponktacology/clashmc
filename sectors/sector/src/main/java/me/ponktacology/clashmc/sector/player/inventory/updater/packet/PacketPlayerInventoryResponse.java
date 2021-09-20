package me.ponktacology.clashmc.sector.player.inventory.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.player.inventory.PlayerInventoryUpdate;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-inventory-response")
public class PacketPlayerInventoryResponse implements PacketUpdate<PlayerInventoryUpdate> {
  
  private final UUID player;
  
  private final Inventory inventoryUpdate;
}

package me.ponktacology.clashmc.safe.item.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.safe.item.SafeItem;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-safe-item-remove")
public class PacketSafeItemRemove implements PacketUpdate<SafeItem> {
  
  private final UUID uuid;
}

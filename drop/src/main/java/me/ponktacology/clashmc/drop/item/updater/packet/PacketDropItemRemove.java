package me.ponktacology.clashmc.drop.item.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.drop.item.DropItem;


@Data
@PacketManifest(channel = "packet-drop-item-remove")
public class PacketDropItemRemove implements PacketUpdate<DropItem> {
  
  private final String name;
}

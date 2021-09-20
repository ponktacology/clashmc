package me.ponktacology.clashmc.itemshop.player.purchase.announcer.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-itemshop-purchase-announce")
public class PacketItemShopPurchaseAnnounce implements PacketAnnounce {

  private final UUID player;

  private final String product;
}

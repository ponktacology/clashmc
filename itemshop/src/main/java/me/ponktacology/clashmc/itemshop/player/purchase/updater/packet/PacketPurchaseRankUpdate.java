package me.ponktacology.clashmc.itemshop.player.purchase.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.itemshop.player.purchase.ItemShopPurchase;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-purchase-rank-update")
public class PacketPurchaseRankUpdate implements PacketUpdate<ItemShopPurchase> {

  private final UUID player;

  private final Grant grant;
}

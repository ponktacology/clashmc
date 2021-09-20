package me.ponktacology.clashmc.itemshop.player.purchase.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.itemshop.player.purchase.ItemShopPurchase;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-purchase-crate-update")
public class PacketPurchaseCrateUpdate implements PacketUpdate<ItemShopPurchase> {

  private final UUID player;

  private final String crate;
  private final int amount;
}

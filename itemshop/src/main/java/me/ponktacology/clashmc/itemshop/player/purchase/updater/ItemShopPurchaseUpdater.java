package me.ponktacology.clashmc.itemshop.player.purchase.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import me.ponktacology.clashmc.itemshop.player.purchase.ItemShopPurchase;
import me.ponktacology.clashmc.itemshop.player.purchase.type.ItemShopPurchaseCrate;
import me.ponktacology.clashmc.itemshop.player.purchase.type.ItemShopPurchaseRank;
import me.ponktacology.clashmc.itemshop.player.purchase.updater.packet.PacketPurchaseCrateUpdate;
import me.ponktacology.clashmc.itemshop.player.purchase.updater.packet.PacketPurchaseRankUpdate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ItemShopPurchaseUpdater implements Updater {

  private final NetworkService networkService;
  private final ItemShopPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;
  private final CrateCache crateCache;
  
  public void update( ItemShopPurchase purchase) {
    ItemShopPlayer itemShopPlayer = purchase.getPlayer();

    if (purchase instanceof ItemShopPurchaseCrate) {
      ItemShopPurchaseCrate purchaseCrate = (ItemShopPurchaseCrate) purchase;
      this.updateCrate(purchaseCrate);
    } else if (purchase instanceof ItemShopPurchaseRank) {
      ItemShopPurchaseRank purchaseRank = (ItemShopPurchaseRank) purchase;
      this.updateRank(purchaseRank);
    }

    if (!this.corePlayerCache.isOnlineNotInAuthOrLobby(itemShopPlayer)) {
      if (purchase instanceof ItemShopPurchaseCrate) {
        ItemShopPurchaseCrate purchaseCrate = (ItemShopPurchaseCrate) purchase;

        itemShopPlayer.addItem(
            new ItemBuilder(purchaseCrate.getCrate().getCrateItem())
                .amount(purchaseCrate.getAmount())
                .build()
                .clone());
      } else if (purchase instanceof ItemShopPurchaseRank) {
        ItemShopPurchaseRank purchaseRank = (ItemShopPurchaseRank) purchase;
        itemShopPlayer.addRank(purchaseRank.getGrant());
      }

      itemShopPlayer.save();
    }
  }

  @PacketHandler
  public void onPacketUpdateCrate( PacketPurchaseCrateUpdate packet) {
    Player player = Bukkit.getPlayer(packet.getPlayer());
    if (player == null) return;

    ItemShopPlayer itemShopPlayer = this.playerCache.getOrKick(player);

    Optional<Crate> crateOptional = this.crateCache.get(packet.getCrate());

    if (!crateOptional.isPresent()) {
      log.info(
          "Received purchase crate update but crate not found, crate= " + packet.getCrate());
      return;
    }

    Crate crate = crateOptional.get();
    itemShopPlayer.addItem(
        new ItemBuilder(crate.getCrateItem()).amount(packet.getAmount()).build().clone());
  }

  @PacketHandler
  public void onPacketUpdateCrate( PacketPurchaseRankUpdate packet) {
    Player player = Bukkit.getPlayer(packet.getPlayer());
    if (player == null) return;

    ItemShopPlayer itemShopPlayer = this.playerCache.getOrKick(player);

    assert packet.getGrant() != null;

    itemShopPlayer.addRank(packet.getGrant());
    itemShopPlayer.initRanks();
  }

  private void updateCrate( ItemShopPurchaseCrate purchaseCrate) {
    this.networkService.publish(
        new PacketPurchaseCrateUpdate(
            purchaseCrate.getPlayer().getUuid(),
            purchaseCrate.getCrate().getName(),
            purchaseCrate.getAmount()));
  }

  private void updateRank( ItemShopPurchaseRank purchaseRank) {
    this.networkService.publish(
        new PacketPurchaseRankUpdate(purchaseRank.getPlayer().getUuid(), purchaseRank.getGrant()));
  }
}

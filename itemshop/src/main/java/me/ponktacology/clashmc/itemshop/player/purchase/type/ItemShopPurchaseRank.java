package me.ponktacology.clashmc.itemshop.player.purchase.type;

import lombok.Getter;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.purchase.ItemShopPurchase;


import java.util.Optional;

@Getter
public class ItemShopPurchaseRank extends ItemShopPurchase {

  private final Grant grant;

  public ItemShopPurchaseRank(ItemShopPlayer player, Grant grant) {
    super(player);
    this.grant = grant;
  }


  @Override
  public String getFormattedName() {
    Optional<Rank> rankOptional = CorePlugin.INSTANCE.getRankCache().get(this.grant.getRankName());

    if (!rankOptional.isPresent()) {
      return "rangę " + this.grant.getRankName();
    }

    Rank rank = rankOptional.get();

    return "rangę " + rank.getFormattedName();
  }
}

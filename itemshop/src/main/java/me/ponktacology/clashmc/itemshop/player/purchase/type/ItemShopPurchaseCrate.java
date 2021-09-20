package me.ponktacology.clashmc.itemshop.player.purchase.type;

import lombok.Getter;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.purchase.ItemShopPurchase;


@Getter
public class ItemShopPurchaseCrate extends ItemShopPurchase {

  private final Crate crate;
  private final int amount;

  public ItemShopPurchaseCrate(ItemShopPlayer player, Crate crate, int amount) {
    super(player);
    this.crate = crate;
    this.amount = amount;
  }


  @Override
  public String getFormattedName() {
    return this.amount + "x skrzynka &f" + this.crate.getName();
  }
}

package me.ponktacology.clashmc.itemshop.player.purchase;

import lombok.Data;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;


@Data
public abstract class ItemShopPurchase {
  
  private final ItemShopPlayer player;
  
  public abstract String getFormattedName();
}

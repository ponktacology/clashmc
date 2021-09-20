package me.ponktacology.clashmc.itemshop.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;

public class ItemShopPlayerFactory extends BukkitPlayerFactory<ItemShopPlayer> {
  public ItemShopPlayerFactory() {
    super(ItemShopPlayer.class, ItemShopPlayer::new);
  }
}

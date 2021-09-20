package me.ponktacology.clashmc.itemshop.player.cache;

import me.ponktacology.clashmc.api.util.MojangUtil;
import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;


public class ItemShopPlayerCache extends BukkitPlayerCache<ItemShopPlayer> {

  public ItemShopPlayerCache(BukkitPlayerFactory<ItemShopPlayer> playerFactory) {
    super(playerFactory);
  }

  public ItemShopPlayer getOrCreate( String name) {
    return super.get(name)
        .orElse(playerFactory.create(MojangUtil.getUuid(name, false).get(), name));
  }
}

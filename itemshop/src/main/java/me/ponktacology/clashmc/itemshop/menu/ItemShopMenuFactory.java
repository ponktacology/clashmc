package me.ponktacology.clashmc.itemshop.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.MenuFactory;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import me.ponktacology.clashmc.itemshop.player.menu.ItemShopMenu;


@RequiredArgsConstructor
public class ItemShopMenuFactory implements MenuFactory {


  private final ItemShopPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;


  public ItemShopMenu getItemShopMenu() {
    return new ItemShopMenu(this.playerCache, this.taskDispatcher);
  }
}

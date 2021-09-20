package me.ponktacology.clashmc.kit.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.MenuFactory;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.kit.menu.KitSelectorMenu;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;


@RequiredArgsConstructor
public class KitMenuFactory implements MenuFactory {


  private final KitPlayerCache playerCache;

  private final KitCache kitCache;


  public KitSelectorMenu getKitSelectorMenu() {
    return new KitSelectorMenu(this.kitCache, this.playerCache);
  }
}

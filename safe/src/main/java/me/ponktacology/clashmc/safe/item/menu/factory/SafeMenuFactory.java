package me.ponktacology.clashmc.safe.item.menu.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.MenuFactory;
import me.ponktacology.clashmc.safe.item.cache.SafeItemCache;
import me.ponktacology.clashmc.safe.item.menu.SafeMenu;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;


@RequiredArgsConstructor
public class SafeMenuFactory implements MenuFactory {


  private final SafeItemCache itemCache;

  private final SafePlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;


  public SafeMenu getSafeMenu() {
    return new SafeMenu(this.itemCache, this.playerCache, this.taskDispatcher);
  }
}

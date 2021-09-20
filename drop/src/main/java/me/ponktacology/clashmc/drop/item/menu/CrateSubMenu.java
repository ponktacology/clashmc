package me.ponktacology.clashmc.drop.item.menu;

import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.crate.crate.item.CrateItem;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.button.CrateItemButton;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.entity.Player;


import java.util.Locale;
import java.util.Map;

public class CrateSubMenu extends SubMenu {

  private final Crate crate;
  private final CrateCache crateCache;
  private final DropItemCache dropItemCache;
  private final DropPlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;

  public CrateSubMenu(
      Crate crate,
      CrateCache crateCache,
      DropItemCache dropItemCache,
      DropPlayerCache playerCache,
      TaskDispatcher taskDispatcher) {
    super(18);
    this.crate = crate;
    this.crateCache = crateCache;
    this.dropItemCache = dropItemCache;
    this.playerCache = playerCache;
    this.taskDispatcher = taskDispatcher;
    {
      setPlaceholder(true);
    }
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);

    for (CrateItem item : crate.getItems()) {
      buttons.put(item.getIndex(), new CrateItemButton(item));
    }

    return buttons;
  }


  @Override
  public String getTitle(Player player) {
    return "&eDrop ze skrzynki " + crate.getName().toLowerCase(Locale.ROOT);
  }


  @Override
  public Menu getPrevious(Player player) {
    return new DropMenu(this.crateCache, this.dropItemCache, this.playerCache, this.taskDispatcher);
  }
}

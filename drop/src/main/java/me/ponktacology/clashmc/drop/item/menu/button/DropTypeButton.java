package me.ponktacology.clashmc.drop.item.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.CobbleXSubMenu;
import me.ponktacology.clashmc.drop.item.menu.LeavesSubMenu;
import me.ponktacology.clashmc.drop.item.menu.StoneSubMenu;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class DropTypeButton extends Button {


  private final DropType type;

  private final DropItemCache dropItemCache;

  private final DropPlayerCache playerCache;

  private final CrateCache crateCache;

  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(this.type.getIcon())
        .name(this.type.getButtonTitle())
        .lore( "&7Kliknij, aby przejść.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {

      switch (type) {
        case COBBLEX:
          new CobbleXSubMenu(dropItemCache, playerCache, taskDispatcher, crateCache)
              .openMenu(player);
          break;
        case STONE:
          new StoneSubMenu(dropItemCache, playerCache, taskDispatcher, crateCache)
              .openMenu(player);
          break;
        case LEAVES:
          new LeavesSubMenu(dropItemCache, playerCache, taskDispatcher, crateCache)
              .openMenu(player);
          break;
      }
    }
  }
}

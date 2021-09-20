package me.ponktacology.clashmc.kit.kit.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.kit.kit.Kit;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.kit.menu.KitMenu;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class KitButton extends Button {


  private final Kit kit;

  private final KitPlayerCache playerCache;

  private final KitCache kitCache;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(kit.getIcon().clone())
        .name("&e" + kit.getName())
        .lore("&7Kliknij, aby wyświetlić zawartość zestawu.")
        .build();
  }

  @Override
  public void clicked(Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      new KitMenu(playerCache, kitCache, kit).openMenu(player);
      return;
    }

    super.clicked(player, clickType);
  }
}

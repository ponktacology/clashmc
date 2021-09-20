package me.ponktacology.clashmc.drop.item.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.CrateSubMenu;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.Locale;

@RequiredArgsConstructor
public class CrateButton extends Button {


  private final Crate crate;

  private final CrateCache crateCache;

  private final DropItemCache dropItemCache;

  private final DropPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.CHEST)
        .name("&eDrop ze skrzynki " + this.crate.getName().toLowerCase(Locale.ROOT))
        .lore( "&7Kliknij, aby przejść.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      new CrateSubMenu(this.crate, this.crateCache, this.dropItemCache, this.playerCache, this.taskDispatcher).openMenu(player);
    }
  }
}

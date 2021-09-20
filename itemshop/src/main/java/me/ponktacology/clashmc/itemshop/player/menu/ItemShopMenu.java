package me.ponktacology.clashmc.itemshop.player.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ItemShopMenu extends Menu {


  private final ItemShopPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;


  @Override
  public String getTitle(Player player) {
    return "&eOdbierz";
  }


  @Override
  public Map<Integer, Button> getButtons( Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    ItemShopPlayer itemShopPlayer = this.playerCache.getOrKick(player);

    for (ItemStack itemStack : itemShopPlayer.getItems()) {
      if (itemStack.getAmount() > itemStack.getMaxStackSize()) {
        int amountLeft = itemStack.getAmount();

        while (amountLeft > 0) {
          int change = Math.min(amountLeft, itemStack.getMaxStackSize());

          buttons.put(
              buttons.size(),
              new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                  return new ItemBuilder(itemStack.clone()).amount(change).build();
                }
              });

          amountLeft -= change;
        }
      } else {
        buttons.put(
            buttons.size(),
            new Button() {
              @Override
              public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(itemStack).build();
              }
            });
      }
    }

    buttons.put(
        44,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.INK_SACK)
                .durability(10)
                .name("&aOdbierz")
                .lore("&7Kliknij, aby odebrać przedmioty.")
                .build();
          }

          @Override
          public void clicked( Player player,  ClickType clickType) {
            if (clickType.isLeftClick()) {
              ItemShopPlayer itemShopPlayer = playerCache.getOrKick(player);

              if (itemShopPlayer == null) {
                return;
              }

              List<ItemStack> items = itemShopPlayer.getItems();

              if (items.isEmpty()) {
                player.sendMessage(Text.colored("&cNie masz żadnych przedmiotów do odebrania."));
                return;
              }

              player.getOpenInventory().close();
              player.closeInventory();

              InventoryUtil.addItem(player, items);
              itemShopPlayer.clearItems();

              taskDispatcher.runAsync(itemShopPlayer::save);

              player.sendMessage(Text.colored("&aOdebrano przedmioty."));
              return;
            }

            super.clicked(player, clickType);
          }
        });

    return buttons;
  }
}

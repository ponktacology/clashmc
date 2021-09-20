package me.ponktacology.clashmc.kit.kit.menu;

import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.kit.kit.Kit;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.player.KitPlayer;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;



import java.util.Map;
import java.util.Optional;

public class KitMenu extends SubMenu {

  private final KitPlayerCache playerCache;
  private final KitCache kitCache;
  private final Kit kit;

  public KitMenu(KitPlayerCache playerCache, KitCache kitCache, Kit kit) {
    super(36, false);
    this.playerCache = playerCache;
    this.kitCache = kitCache;
    this.kit = kit;
  }

  {
    setPlaceholder(true);
  }

  @Override
  public Map<Integer, Button> getButtons( Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);

    int i = 1;
    int row = 9;

    for (ItemStack item : kit.getItems()) {
      if (item == null) continue;
      buttons.put(
          row + i++,
          new Button() {
            
            @Override
            public ItemStack getButtonItem(Player player) {
              return item;
            }
          });

      if (i == 8) {
        i = 1;
        row += 9;
      }
    }

    Optional<KitPlayer> kitPlayerOptional = this.playerCache.get(player);

    if (!kitPlayerOptional.isPresent()) {
      return buttons;
    }

    KitPlayer kitPlayer = kitPlayerOptional.get();

    buttons.put(
        43,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return kitPlayer.hasAccess(kit)
                ? new ItemBuilder(Material.INK_SACK)
                    .durability(10)
                    .name("&aOdbierz zestaw")
                    .lore("&7Kliknij, aby odebrać zestaw.")
                    .build()
                : new ItemBuilder(Material.INK_SACK)
                    .durability(8)
                    .name("&cNie masz dostępu")
                    .lore("&7Zakup rangę, aby otrzymać dostęp do tego zestawu.")
                    .build();
          }

          @Override
          public void clicked( Player player,  ClickType clickType) {
            if (clickType.isLeftClick()) {
              if (kitPlayer.handleKit(kit)) {
                player.getOpenInventory().close();
                player.closeInventory();
                return;
              }
            }

            super.clicked(player, clickType);
          }
        });

    return buttons;
  }


  @Override
  public String getTitle(Player player) {
    return "&ePodgląd zestawu";
  }


  @Override
  public Menu getPrevious(Player player) {
    return new KitSelectorMenu(this.kitCache, this.playerCache);
  }
}

package me.ponktacology.clashmc.core.menu.pagination;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class SubMenu extends Menu implements ISubMenu {

  private final int size;
  private final boolean extendByOneRow;

    public SubMenu(int size) {
        this.size = size;
        this.extendByOneRow = true;
    }

    @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    int lastRow = size + (extendByOneRow ? 9 : 0);
    for (int x = lastRow; x < lastRow + 9; x++) {
      buttons.put(x, getPlaceholderButton());
    }

    buttons.put(
        lastRow + 4,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.FENCE_GATE).name(ChatColor.YELLOW + "Powrót").build();
          }

          @Override
          public void clicked( Player player,  ClickType clickType) {
            if (clickType.isLeftClick()) {
                Menu previous = getPrevious(player);

                if(previous != null) {
                    previous.openMenu(player);
                }
              return;
            }
            super.clicked(player, clickType);
          }
        });

    return buttons;
  }
}

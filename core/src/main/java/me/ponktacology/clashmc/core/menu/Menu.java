package me.ponktacology.clashmc.core.menu;

import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public abstract class Menu {


  public static Map<UUID, Menu> currentlyOpenedMenus = new HashMap<>();
  private Map<Integer, Button> buttons = new HashMap<>();
  private boolean autoUpdate;
  private boolean updateAfterClick;
  private boolean closedByMenu;
  private boolean placeholder;

  private Button placeholderButton =
      Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 0, ChatColor.GRAY + ", ");
  private boolean canAddItems;

  private ItemStack createItemStack(Player player,  Button button) {
    ItemStack item = button.getButtonItem(player);

    if (item.getType() != Material.SKULL_ITEM) {
      ItemMeta meta = item.getItemMeta();

      if (meta != null && meta.hasDisplayName()) {
        meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
      }

      item.setItemMeta(meta);
    }

    return item;
  }

  public void openMenu( final Player player) {
    this.buttons = this.getButtons(player);

    Menu previousMenu = Menu.currentlyOpenedMenus.get(player.getUniqueId());
    Inventory inventory = null;
    int size = getSize() != -1 ? getSize() : this.size(this.buttons);

    boolean update = false;
    String title = Text.colored(this.getTitle(player));

    if (title.length() > 32) {
      title = title.substring(0, 32);
    }

    if (player.getOpenInventory() != null) {
      if (previousMenu == null) {
        player.closeInventory();
      } else {
        int previousSize = player.getOpenInventory().getTopInventory().getSize();

        if (previousSize == size
            && player.getOpenInventory().getTopInventory().getTitle().equals(title)) {
          inventory = player.getOpenInventory().getTopInventory();
          update = true;
        } else {
          previousMenu.setClosedByMenu(true);
          player.closeInventory();
        }
      }
    }

    if (inventory == null) {
      inventory = Bukkit.createInventory(player, size, title);
    }

    inventory.setContents(new ItemStack[inventory.getSize()]);

    currentlyOpenedMenus.put(player.getUniqueId(), this);

    for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
      inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
    }

    if (this.isPlaceholder()) {
      for (int index = 0; index < size; index++) {
        if (this.buttons.get(index) == null) {
          this.buttons.put(index, this.placeholderButton);
          inventory.setItem(index, this.placeholderButton.getButtonItem(player));
        }
      }
    }

    if (update) {
      player.updateInventory();
    } else {
      player.openInventory(inventory);
    }

    this.onOpen(player);
    this.setClosedByMenu(false);
  }

  public int size( Map<Integer, Button> buttons) {
    int highest = 0;

    for (int buttonValue : buttons.keySet()) {
      if (buttonValue > highest) {
        highest = buttonValue;
      }
    }

    return (int) (Math.ceil((highest + 1) / 9D) * 9D);
  }

  public int getSize() {
    return -1;
  }

  public int getSlot(int x, int y) {
    return ((9 * y) + x);
  }

  public abstract String getTitle(Player player);

  public abstract Map<Integer, Button> getButtons(Player player);

  public void onOpen(Player player) {}

  public void onClose(Player player) {}
}

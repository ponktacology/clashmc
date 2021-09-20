package me.ponktacology.clashmc.safe.item.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.safe.item.SafeItem;
import me.ponktacology.clashmc.safe.item.cache.SafeItemCache;
import me.ponktacology.clashmc.safe.item.menu.button.SafeItemButton;
import me.ponktacology.clashmc.safe.player.SafePlayer;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class SafeMenu extends Menu {

  private final SafeItemCache itemCache;

  private final SafePlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  {
    setPlaceholder(true);
  }

  @Override
  public String getTitle(Player player) {
    return "&eSchowek";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    Set<SafeItem> items = this.itemCache.values();
    int rows = (int) Math.ceil(items.size() / 9.0);

    for (SafeItem item : items) {
      buttons.put(item.getIndex(), new SafeItemButton(item, this.playerCache, this.taskDispatcher));
    }

    buttons.put(
        (rows * 9) + 4,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.HOPPER)
                .name("&eWyrównaj do limitu")
                .lore(
                    "&7Kliknij, aby wyrównać każdy przedmiot z limitem.",
                    "&7Kliknij shift + lewy, aby wypłacić wszystkie przedmioty.")
                .build();
          }

          @Override
          public boolean shouldUpdate(Player player, ClickType clickType) {
            if (clickType.isLeftClick()) {
              Optional<SafePlayer> safePlayerOptional = playerCache.get(player);

              if (!safePlayerOptional.isPresent()) {
                return false;
              }

              SafePlayer safePlayer = safePlayerOptional.get();

              boolean isShiftClick = clickType.isShiftClick();

              for (SafeItem item : itemCache.values()) {
                if (isShiftClick) {
                  safePlayer.withdrawAll(item);
                } else safePlayer.withdrawToLimit(item);
              }

              player.sendMessage(
                  Text.colored(
                      isShiftClick
                          ? "&aWypłacono wszystkie przedmioty."
                          : "&aWyrównano do limitu."));
              taskDispatcher.runAsync(safePlayer::save);
              return true;
            }
            return super.shouldUpdate(player, clickType);
          }
        });

    return buttons;
  }
}

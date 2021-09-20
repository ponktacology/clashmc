package me.ponktacology.clashmc.drop.item.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.player.data.PlayerDropData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class DropItemButton extends Button {

  
  private final DropItem item;
  
  private final DropPlayerCache playerCache;
  
  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem( Player player) {
    DropPlayer dropPlayer = this.playerCache.getOrKick(player);
    PlayerDropData dropData = dropPlayer.getDropData(item);

    ItemBuilder itemBuilder =
        new ItemBuilder(item.getItem().clone()).name(item.getDisplayName()).amount(1);

    if (item.getType() == DropType.STONE) {
      itemBuilder.lore(
          "",
          "&7Szansa: &f"
              + item.getChance()
              + "%"
              + (dropData.getLevel() > 0
                  ? " &9(Bonus: &f" + MathUtil.roundOff(dropData.getBonus(), 2) + "%&9)"
                  : ""),
          "&7Poniżej poziomu Y: &f" + item.getDropBelowY(),
          "&7Fortune: " + StyleUtil.convertBooleanToText(item.isFortune()),
          "&7Włączony: " + StyleUtil.convertBooleanToText(dropPlayer.isItemEnabled(item)),
          "");

      if (dropData.isMaxLevel()) {
        itemBuilder.lore("&aOsiągnięto maksymalny poziom ulepszenia.");
      } else {
        itemBuilder.lore(
            "&7Poziom ulepszenia: &f" + dropData.getLevel(),
            "&7Koszt ulepszenia: &f" + dropData.getUpgradePrice() + " &7punktów kopania",
            "&7Kliknij shift + lewy, aby ulepszyć.");
      }
    }

    return itemBuilder.build();
  }

  @Override
  public boolean shouldUpdate( Player player,  ClickType clickType) {
    DropPlayer dropPlayer = this.playerCache.getOrKick(player);

    PlayerDropData dropData = dropPlayer.getDropData(item);

    if (item.getType() == DropType.STONE) {
      if (clickType.isShiftClick() && clickType.isLeftClick()) {

        if (dropData.isMaxLevel()) {
          player.sendMessage(Text.colored("&cOsiągnięto maksymalny poziom ulepszenia."));
          return super.shouldUpdate(player, clickType);
        }

        if (dropPlayer.getPoints() < dropData.getUpgradePrice()) {
          player.sendMessage(Text.colored("&cNie masz wystarczającej ilości punktów."));
          return super.shouldUpdate(player, clickType);
        }

        dropPlayer.decreasePoints(dropData.getUpgradePrice());
        dropData.increaseLevel();

        this.taskDispatcher.runAsync(dropPlayer::save);
      } else if (clickType.isLeftClick()) {
        dropPlayer.toggleItem(item);

        this.taskDispatcher.runAsync(dropPlayer::save);
      }

      return true;
    }

    return super.shouldUpdate(player, clickType);
  }
}

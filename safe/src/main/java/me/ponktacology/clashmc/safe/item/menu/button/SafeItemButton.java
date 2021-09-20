package me.ponktacology.clashmc.safe.item.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.safe.item.SafeItem;
import me.ponktacology.clashmc.safe.player.SafePlayer;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;



import java.util.Optional;

@RequiredArgsConstructor
public class SafeItemButton extends Button {


  private final SafeItem item;

  private final SafePlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  
  @Override
  public ItemStack getButtonItem( Player player) {
    Optional<SafePlayer> safePlayerOptional = this.playerCache.get(player);

    if (!safePlayerOptional.isPresent()) {
      return null;
    }

    SafePlayer safePlayer = safePlayerOptional.get();

    int count = safePlayer.getItemCount(item);
    ItemStack itemStack = this.item.getItem();

    return new ItemBuilder(itemStack.getType())
        .durability(itemStack.getDurability())
        .name(this.item.getName())
        .amount(count)
        .lore(
            "&eW schowku: &f" + count,
            "&eLimit: &f" + this.item.getLimit(),
            "&7Kliknij prawym, aby wypłacić do limitu.")
        .build();
  }

  @Override
  public boolean shouldUpdate( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      Optional<SafePlayer> safePlayerOptional = this.playerCache.get(player);

      if (!safePlayerOptional.isPresent()) {
        return false;
      }

      SafePlayer safePlayer = safePlayerOptional.get();

      if (safePlayer.withdrawToLimit(item, true)) {
        player.sendMessage(Text.colored("&aWypłacono do limitu."));
        this.taskDispatcher.runAsync(safePlayer::save);
      }

      return true;
    }

    return super.shouldUpdate(player, clickType);
  }
}

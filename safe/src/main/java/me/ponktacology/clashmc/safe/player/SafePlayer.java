package me.ponktacology.clashmc.safe.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.safe.item.SafeItem;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity(collection = "players", database = "safe")
public class SafePlayer extends BukkitPlayerWrapper {

  @Getter private final Map<UUID, Integer> items = new HashMap<>();

  public SafePlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public int getWithdraw(Player player, SafeItem item) {
    return Math.min(item.getLimit() - getItemCountInInventory(player, item), getItemCount(item));
  }

  public void withdraw(SafeItem item, int count) {
    items.put(item.getUuid(), getItemCount(item) - count);
  }

  public void withdrawAll(SafeItem item) {
    int count = getItemCount(item);
    if (count == 0) return;
    withdraw(item, count);
    addToInventory(item, count);
  }

  public boolean withdrawToLimit(SafeItem item) {
    return withdrawToLimit(item, false);
  }

  public boolean withdrawToLimit(SafeItem item, boolean inform) {
    Player player = getPlayer();

    if (player == null) {
      return false;
    }

    int count = getWithdraw(player, item);

    if (count < 0) {
      if (inform) {
        player.sendMessage(Text.colored("&cPosiadasz już limit tego przedmiotu w ekwipunku."));
      }
      return false;
    }

    if (count == 0) {
      if (inform) {
        player.sendMessage(Text.colored("&cNie posiadasz tego przedmiotu w schowku."));
      }
      return false;
    }

    withdraw(item, count);
    addToInventory(item, count);
    return true;
  }

  public void addItems(SafeItem item, int count) {
    this.items.put(item.getUuid(), this.items.getOrDefault(item.getUuid(), 0) + count);
  }

  public void addToInventory(SafeItem item, int count) {
    Player player = getPlayer();

    if (player == null) {
      return;
    }

    InventoryUtil.addItem(player, new ItemBuilder(item.getItem().clone()).amount(count).build());
  }

  public int getItemCountInInventory(Player player, SafeItem item) {
    return InventoryUtil.countItemsIgnoreItemMeta(player, item.getItem());
  }

  public int getItemCount(SafeItem safeItem) {
    return this.items.computeIfAbsent(safeItem.getUuid(), type -> 0);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SafePlayer that = (SafePlayer) o;

    return this.getUuid().equals(that.getUuid());
  }
}

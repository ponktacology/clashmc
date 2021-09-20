package me.ponktacology.clashmc.core.menu.pagination;

import lombok.AllArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@AllArgsConstructor
public class PageButton extends Button {

  private int mod;
  private PaginatedMenu menu;

  @Override
  public ItemStack getButtonItem(Player player) {
    if (this.mod > 0) {
      if (hasNext(player)) {
        return new ItemBuilder(Material.REDSTONE_TORCH_ON)
            .name(ChatColor.GREEN + "Następna strona")
            .build();
      } else {
        return new ItemBuilder(Material.BARRIER)
            .name(ChatColor.GRAY + "Brak następnej strony")
            .build();
      }
    } else {
      if (hasPrevious(player)) {
        return new ItemBuilder(Material.REDSTONE_TORCH_ON)
            .name(ChatColor.GREEN + "Poprzednia strona")
            .build();
      } else {
        return new ItemBuilder(Material.BARRIER)
                .name(ChatColor.GRAY + "Brak poprzedniej strony")
                .build();
      }
    }
  }

  @Override
  public void clicked( Player player, ClickType clickType) {
    if (this.mod > 0) {
      if (hasNext(player)) {
        this.menu.modPage(player, this.mod);
        Button.playNeutral(player);
      } else {
        Button.playFail(player);
      }
    } else {
      if (hasPrevious(player)) {
        this.menu.modPage(player, this.mod);
        Button.playNeutral(player);
      } else {
        Button.playFail(player);
      }
    }
  }

  private boolean hasNext(Player player) {
    int pg = this.menu.getPage() + this.mod;
    return this.menu.getPages(player) >= pg;
  }

  private boolean hasPrevious(Player player) {
    int pg = this.menu.getPage() + this.mod;
    return pg > 0;
  }
}

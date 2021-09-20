package me.ponktacology.clashmc.guild.guild.panel.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.panel.GuildManagePlayersMenu;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GuildManagePlayersButton extends Button {

  private final Guild guild;
  private final GuildPlayerCache playerCache = GuildPlugin.INSTANCE.getPlayerCache();

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.BOOKSHELF)
        .name("&eZarządzanie rangami członków gildii")
        .lore("&7Kliknij, aby przejść do zarządzania rangami członków gildii.")
        .build();
  }

  @Override
  public void clicked(Player player, ClickType clickType) {
    if (clickType.isLeftClick()) {
      GuildPlayer guildPlayer = playerCache.getOrKick(player);

      if (!guild.hasPermission(guildPlayer, GuildPermission.Permissions.RANK_CHANGE)) {
        player.sendMessage(
            Text.colored(
                "&cNie posiadasz permisji do zarządzania rangami członków gildii. Poproś lidera o zmianę ich w /g panel."));
        return;
      }

      new GuildManagePlayersMenu(guild).openMenu(player);
      return;
    }

    super.clicked(player, clickType);
  }
}

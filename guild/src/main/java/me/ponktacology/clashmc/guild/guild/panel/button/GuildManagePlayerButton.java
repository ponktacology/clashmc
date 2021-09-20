package me.ponktacology.clashmc.guild.guild.panel.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.panel.GuildManagePlayerMenu;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GuildManagePlayerButton extends Button {

  private final Guild guild;
  private final GuildPlayer guildPlayer;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.SKULL_ITEM)
        .skull(guildPlayer.getName())
        .name("&e" + guildPlayer.getName())
        .lore("&e")
        .lore("&7Kliknij, aby zarządzać tym graczem.")
        .build();
  }

  @Override
  public void clicked(Player player, ClickType clickType) {
    if(clickType.isLeftClick()) {
      new GuildManagePlayerMenu(guild, guildPlayer).openMenu(player);
    }
  }
}

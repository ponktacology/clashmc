package me.ponktacology.clashmc.guild.guild.panel.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.panel.GuildEditRolePermissionsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GuildRoleButton extends Button {

  private final Guild guild;
  private final GuildRole role;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(role.getIcon())
        .name("&e" + role.getFormattedName())
        .lore("&7Kliknij, aby edytować tę rangę.")
        .build();
  }

  @Override
  public void clicked(Player player, ClickType clickType) {
    if (clickType.isLeftClick()) {

      new GuildEditRolePermissionsMenu(guild, role).openMenu(player);
    }
  }
}

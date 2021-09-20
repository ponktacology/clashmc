package me.ponktacology.clashmc.guild.guild.panel;

import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.panel.button.GuildPermissionButton;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;

public class GuildEditRolePermissionsMenu extends SubMenu {

  private final Guild guild;
  private final GuildRole role;


  public GuildEditRolePermissionsMenu(Guild guild, GuildRole role) {
    super(18);
    this.guild = guild;
    this.role = role;
  }

  @Override
  public String getTitle(Player player) {
    return "&eEdytowanie rangi &f" + role.getFormattedName().toLowerCase(Locale.ROOT);
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);
    GuildPermission permissions = guild.getPermission(role);

    for (GuildPermission.Permissions permission : permissions.getPermissions()) {
      buttons.put(buttons.size() - 9, new GuildPermissionButton(guild, role, permission));
    }

    return buttons;
  }

  @Override
  public Menu getPrevious(Player player) {
    return new GuildRoleSelectMenu(guild);
  }
}

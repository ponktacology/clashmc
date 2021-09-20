package me.ponktacology.clashmc.guild.guild.panel;

import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.panel.button.GuildPlayerRoleButton;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import org.bukkit.entity.Player;

import java.util.Map;

public class GuildManagePlayerMenu extends SubMenu {

  private final Guild guild;
  private final GuildPlayer guildPlayer;

  public GuildManagePlayerMenu(Guild guild, GuildPlayer guildPlayer) {
    super(18);
    this.guild = guild;
    this.guildPlayer = guildPlayer;
  }

  @Override
  public String getTitle(Player player) {
    return "&eZarządzanie rangą";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);

    for (GuildRole role : GuildRole.values()) {
      if (!guild.canManage(guildPlayer, role)) continue;

      buttons.put(buttons.size() - 9, new GuildPlayerRoleButton(guild, role, guildPlayer));
    }

    return buttons;
  }

  @Override
  public Menu getPrevious(Player player) {
    return new GuildManagePlayersMenu(guild);
  }
}

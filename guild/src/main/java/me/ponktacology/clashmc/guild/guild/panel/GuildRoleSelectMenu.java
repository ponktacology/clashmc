package me.ponktacology.clashmc.guild.guild.panel;

import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.panel.button.GuildRoleButton;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;

import java.util.Map;

public class GuildRoleSelectMenu extends SubMenu {

  private final Guild guild;
  private final GuildPlayerCache playerCache = GuildPlugin.INSTANCE.getPlayerCache();

  public GuildRoleSelectMenu(Guild guild) {
    super(9, false);
    this.guild = guild;
  }

  @Override
  public String getTitle(Player player) {
    return "&eWybierz rangę do edycji";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = super.getButtons(player);

    GuildPlayer guildPlayer = playerCache.getOrKick(player);
    for (GuildRole role : GuildRole.values()) {
      if(role == GuildRole.LEADER) continue;
      if (!guild.canManage(guildPlayer, role)) continue;

      buttons.put(buttons.size() - 9, new GuildRoleButton(guild, role));
    }

    return buttons;
  }

  @Override
  public Menu getPrevious(Player player) {
    return new GuildPanelMenu(this.guild);
  }
}

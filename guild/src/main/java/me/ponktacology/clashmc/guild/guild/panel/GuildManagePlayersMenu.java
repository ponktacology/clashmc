package me.ponktacology.clashmc.guild.guild.panel;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedSubMenu;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.panel.button.GuildManagePlayerButton;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;

import java.util.Map;

public class GuildManagePlayersMenu extends PaginatedSubMenu {

  private final Guild guild;
  private final GuildPlayerCache playerCache = GuildPlugin.INSTANCE.getPlayerCache();

  public GuildManagePlayersMenu(Guild guild) {
    super(36);
    this.guild = guild;
  }

  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eZarządzanie członkami gildii";
  }

  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();
    GuildPlayer guildPlayer = playerCache.getOrKick(player);

    for (GuildPlayer otherPlayer : guild.members()) {
      if (!guild.canManage(guildPlayer, otherPlayer)) continue;

      buttons.put(buttons.size(), new GuildManagePlayerButton(guild, otherPlayer));
    }

    return buttons;
  }

  @Override
  public Menu getPrevious(Player player) {
    return new GuildPanelMenu(guild);
  }
}

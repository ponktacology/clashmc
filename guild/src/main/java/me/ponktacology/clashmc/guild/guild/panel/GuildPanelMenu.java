package me.ponktacology.clashmc.guild.guild.panel;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.panel.button.EditGuildRolesButton;
import me.ponktacology.clashmc.guild.guild.panel.button.GuildInfoButton;
import me.ponktacology.clashmc.guild.guild.panel.button.GuildManagePlayersButton;
import org.bukkit.entity.Player;

import java.util.Map;

@RequiredArgsConstructor
public class GuildPanelMenu extends Menu {

  private final Guild guild;

  @Override
  public String getTitle(Player player) {
    return "&ePanel gildii &f" + this.guild.getTag();
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    buttons.put(11, new EditGuildRolesButton(guild));
    buttons.put(13, new GuildInfoButton(guild));
    buttons.put(15, new GuildManagePlayersButton(guild));

    return buttons;
  }

  @Override
  public int getSize() {
    return 27;
  }
}

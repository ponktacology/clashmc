package me.ponktacology.clashmc.guild.player.chat.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;
import me.ponktacology.clashmc.guild.player.chat.menu.button.ChatSettingButton;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ChatSettingsMenu extends Menu {


  private final GuildPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  {
    setPlaceholder(true);
  }


  @Override
  public String getTitle(Player player) {
    return "&eUstawienia czatu";
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    for (ChatSettings.Settings setting : ChatSettings.Settings.values()) {
      buttons.put(
          buttons.size() + 1,
          new ChatSettingButton(setting, this.playerCache, this.taskDispatcher));
    }

    return buttons;
  }
}

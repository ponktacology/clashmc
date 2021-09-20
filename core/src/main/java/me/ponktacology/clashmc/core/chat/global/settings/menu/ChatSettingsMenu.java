package me.ponktacology.clashmc.core.chat.global.settings.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.factory.ChatSettingsFactory;
import me.ponktacology.clashmc.core.chat.global.settings.menu.button.ChatClearButton;
import me.ponktacology.clashmc.core.chat.global.settings.menu.button.ChatDelayButton;
import me.ponktacology.clashmc.core.chat.global.settings.menu.button.ChatPowerButton;
import me.ponktacology.clashmc.core.chat.global.settings.menu.button.ChatStateButton;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatClearUpdater;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatSettingsUpdater;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ChatSettingsMenu extends Menu {

  
  private final ChatSettingsCache chatSettingsCache;
  
  private final ChatSettingsUpdater chatSettingsUpdater;
  
  private final ChatSettingsFactory chatSettingsFactory;
  
  private final ChatClearUpdater chatClearUpdater;
  
  private final TaskDispatcher taskDispatcher;

  {
    setPlaceholder(true);
  }

  
  @Override
  public String getTitle(Player player) {
    return "&eUstawienia chatu";
  }

  
  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    buttons.put(11, new ChatClearButton(chatClearUpdater, taskDispatcher));
    buttons.put(12, new ChatStateButton(chatSettingsCache, chatSettingsUpdater, taskDispatcher));
    buttons.put(
        13,
        new ChatPowerButton(
            chatSettingsUpdater,
            chatSettingsFactory,
            chatSettingsCache,
            chatClearUpdater,
            taskDispatcher));
    buttons.put(
        14,
        new ChatDelayButton(
            chatSettingsUpdater,
            chatSettingsFactory,
            chatSettingsCache,
            chatClearUpdater,
            taskDispatcher));
    buttons.put(18, getPlaceholderButton());

    return buttons;
  }
}

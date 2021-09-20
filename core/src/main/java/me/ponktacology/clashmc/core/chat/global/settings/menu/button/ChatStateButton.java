package me.ponktacology.clashmc.core.chat.global.settings.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatSettingsUpdater;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ChatStateButton extends Button {


  private final ChatSettingsCache chatSettingsCache;

  private final ChatSettingsUpdater chatSettingsUpdater;

  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem(Player player) {
    ChatSettings chatSettings = this.chatSettingsCache.get();

    List<String> lore = new ArrayList<>();

    ChatSettings.ChatState state = chatSettings.getState();

    lore.add("&eAktualny tryb: &f" + state.getFormattedName());
    lore.add("");
    lore.add("&7Kliknij, aby zmienić tryb.");

    return new ItemBuilder(Material.DIODE).name("&eZmień tryb czatu").lore(lore).build();
  }

  @Override
  public boolean shouldUpdate(Player player,  ClickType clickType) {
    ChatSettings chatSettings = this.chatSettingsCache.get();

    if (clickType.isLeftClick()) {
      ChatSettings.ChatState currentState = chatSettings.getState();
      chatSettings.setState(currentState.next());
      this.taskDispatcher.runAsync(() -> this.chatSettingsUpdater.update(chatSettings));
      return true;
    }

    return super.shouldUpdate(player, clickType);
  }
}

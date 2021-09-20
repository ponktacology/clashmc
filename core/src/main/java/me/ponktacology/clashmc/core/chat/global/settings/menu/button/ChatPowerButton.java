package me.ponktacology.clashmc.core.chat.global.settings.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.factory.ChatSettingsFactory;
import me.ponktacology.clashmc.core.chat.global.settings.menu.ChatSettingsMenu;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatClearUpdater;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatSettingsUpdater;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.menus.ValueSubMenu;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class ChatPowerButton extends Button {


  private final ChatSettingsUpdater chatSettingsUpdater;

  private final ChatSettingsFactory chatSettingsFactory;

  private final ChatSettingsCache chatSettingsCache;

  private final ChatClearUpdater chatClearUpdater;

  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.REDSTONE)
        .name("&eZmień limit power czatu")
        .lore( "&7Kliknij, limit power czatu.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      ChatSettingsMenu previousMenu =
          new ChatSettingsMenu(
              chatSettingsCache,
              chatSettingsUpdater,
              chatSettingsFactory,
              chatClearUpdater,
              taskDispatcher);
      final ChatSettings chatSettings = chatSettingsCache.get();
      new ValueSubMenu(
              "&7Limit power czatu",
              chatSettings.getMinPower(),
              0,
              Integer.MAX_VALUE,
              1000,
              100,
              1,
              value -> {
                taskDispatcher.runAsync(
                    () -> {
                      chatSettings.setMinPower(value.intValue());
                      chatSettingsUpdater.update(chatSettings);
                    });
                previousMenu.openMenu(player);
                player.sendMessage(Text.colored("&aZatwierdzono limit power czatu."));
              },
              previousMenu)
          .openMenu(player);
      return;
    }
    super.clicked(player, clickType);
  }
}

package me.ponktacology.clashmc.core.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.factory.ChatSettingsFactory;
import me.ponktacology.clashmc.core.chat.global.settings.menu.ChatSettingsMenu;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatClearUpdater;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatSettingsUpdater;
import me.ponktacology.clashmc.core.log.chat.menu.ChatLogsMenu;
import me.ponktacology.clashmc.core.log.command.menu.CommandLogsMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.menu.AltsMenu;
import me.ponktacology.clashmc.core.punishment.menu.HistoryMenu;
import me.ponktacology.clashmc.core.rank.menu.GrantsMenu;


@RequiredArgsConstructor
public class CoreMenuFactory implements MenuFactory {


  private final CorePlayerFactory playerFactory;

  private final CorePlayerCache playerCache;

  private final ChatSettingsCache chatSettingsCache;

  private final ChatSettingsUpdater chatSettingsUpdater;

  private final ChatSettingsFactory chatSettingsFactory;

  private final ChatClearUpdater chatClearUpdater;

  private final TaskDispatcher taskDispatcher;


  public AltsMenu getAltsMenu(CorePlayer corePlayer) {
    return new AltsMenu(corePlayer, this.playerFactory, this.playerCache, this.taskDispatcher);
  }


  public HistoryMenu getHistoryMenu(CorePlayer corePlayer) {
    return new HistoryMenu(corePlayer, this.playerFactory, this.taskDispatcher);
  }


  public GrantsMenu getGrantsMenu(CorePlayer corePlayer) {
    return new GrantsMenu(corePlayer, this.playerFactory);
  }


  public CommandLogsMenu getCommandLogsMenu(CorePlayer corePlayer) {
    return new CommandLogsMenu(corePlayer);
  }


  public ChatLogsMenu getChatLogsMenu(CorePlayer corePlayer) {
    return new ChatLogsMenu(corePlayer);
  }


  public ChatSettingsMenu getChatSettingsMenu() {
    return new ChatSettingsMenu(
        this.chatSettingsCache,
        this.chatSettingsUpdater,
        this.chatSettingsFactory,
        this.chatClearUpdater,
        this.taskDispatcher);
  }
}

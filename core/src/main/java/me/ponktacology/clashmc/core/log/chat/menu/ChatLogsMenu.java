package me.ponktacology.clashmc.core.log.chat.menu;

import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.log.chat.ChatLog;
import me.ponktacology.clashmc.core.log.chat.menu.button.ChatLogButton;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatLogsMenu extends PaginatedMenu {

  private final CorePlayer corePlayer;

  public ChatLogsMenu(CorePlayer corePlayer) {
    super(36);
    this.corePlayer = corePlayer;
  }

  
  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eHistoria wiadomości gracza &f" + corePlayer.getName();
  }

  
  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    List<ChatLog> chatLogs = corePlayer.getChatLogs();

    for (ChatLog chatLog : chatLogs.stream().sorted((o1, o2) -> (int) -(o1.getTimeStamp() - o2.getTimeStamp())).collect(Collectors.toList())) {
      buttons.put(buttons.size(), new ChatLogButton(chatLog));
    }

    return buttons;
  }
}

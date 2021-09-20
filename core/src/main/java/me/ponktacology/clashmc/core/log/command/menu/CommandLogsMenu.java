package me.ponktacology.clashmc.core.log.command.menu;

import me.ponktacology.clashmc.core.log.command.CommandLog;
import me.ponktacology.clashmc.core.log.command.menu.button.CommandLogButton;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandLogsMenu extends PaginatedMenu {

  private final CorePlayer corePlayer;

  public CommandLogsMenu(CorePlayer corePlayer) {
    super(36);
    this.corePlayer = corePlayer;
  }

  
  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eHistoria komend gracza &f" + corePlayer.getName();
  }

  
  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    List<CommandLog> commandLogs = corePlayer.getCommandLogs();

    for (CommandLog commandLog :
        commandLogs.stream()
            .sorted((o1, o2) -> (int) -(o1.getTimeStamp() - o2.getTimeStamp()))
            .collect(Collectors.toList())) {
      buttons.put(buttons.size(), new CommandLogButton(commandLog));
    }

    return buttons;
  }
}

package me.ponktacology.clashmc.core.punishment.menu;


import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.menu.button.HistoryButton;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HistoryMenu extends PaginatedMenu {

  private final CorePlayer corePlayer;
  private final CorePlayerFactory playerFactory;
  private final TaskDispatcher taskDispatcher;

  public HistoryMenu(
     CorePlayer corePlayer,
      CorePlayerFactory playerFactory,
      TaskDispatcher taskDispatcher) {
    super(18);
    this.corePlayer = corePlayer;
    this.playerFactory = playerFactory;
    this.taskDispatcher = taskDispatcher;
  }


  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eHistoria kar gracza &f" + corePlayer.getName();
  }

  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    CompletableFuture<Map<Integer, Button>> completableFuture = new CompletableFuture<>();
    Map<Integer, Button> buttons = new HashMap<>();

    this.taskDispatcher.runAsync(
        () -> {
          List<Punishment> punishments = this.corePlayer.getPunishments();

          if (punishments.isEmpty()) {
            completableFuture.complete(buttons);
            return;
          }

          for (Punishment punishment :
              punishments.stream()
                  .sorted((o1, o2) -> (int) -(o1.getAddedOn() - o2.getAddedOn()))
                  .collect(Collectors.toList())) {
            buttons.put(buttons.size(), new HistoryButton(punishment, playerFactory));
          }

          completableFuture.complete(buttons);
        });

    try {
      return completableFuture.get(25, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return buttons;
  }
}

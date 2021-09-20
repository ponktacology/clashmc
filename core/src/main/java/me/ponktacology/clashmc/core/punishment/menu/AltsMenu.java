package me.ponktacology.clashmc.core.punishment.menu;


import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.menu.button.AltButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AltsMenu extends PaginatedMenu {

  private final CorePlayer corePlayer;
  private final CorePlayerFactory playerFactory;
  private final CorePlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;

  public AltsMenu(
          CorePlayer corePlayer,
          CorePlayerFactory playerFactory,
          CorePlayerCache playerCache, TaskDispatcher taskDispatcher) {
    super(18);
    this.corePlayer = corePlayer;
    this.playerFactory = playerFactory;
    this.playerCache = playerCache;
    this.taskDispatcher = taskDispatcher;
  }


  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eAlty gracza &f" + corePlayer.getName();
  }

  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    CompletableFuture<Map<Integer, Button>> completableFuture = new CompletableFuture<>();
    Map<Integer, Button> buttons = new HashMap<>();

    this.taskDispatcher.runAsync(
        () -> {
          Set<CorePlayer> alts = this.corePlayer.getAlts();

          if (alts.isEmpty()) {
            completableFuture.complete(buttons);
            return;
          }

          for (CorePlayer alt : alts) {
            buttons.put(buttons.size(), new AltButton(this.playerFactory, playerCache,  alt));
          }

          completableFuture.complete(buttons);
        });

    try {
      return completableFuture.get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return buttons;
  }
}

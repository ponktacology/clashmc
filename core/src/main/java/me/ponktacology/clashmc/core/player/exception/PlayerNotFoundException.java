package me.ponktacology.clashmc.core.player.exception;

import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.entity.Player;


public class PlayerNotFoundException extends Exception {

  public PlayerNotFoundException( Player player, String message) {
    super(message);
    kick(player);
  }

  public PlayerNotFoundException( Player player) {
    this(player, player.getName());
  }

  private void kick( Player player) {
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .run(() -> player.kickPlayer(Text.colored("&cWystąpił błąd :(")));
  }
}

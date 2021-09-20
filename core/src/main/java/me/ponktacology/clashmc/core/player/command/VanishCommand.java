package me.ponktacology.clashmc.core.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class VanishCommand {

  private final TaskDispatcher taskDispatcher;
  private final CorePlayerCache playerCache;

  @Command(
      value = {"vanish", "v"},
      description = "Włącza/Wyłącza vanisha")
  @Permission(CorePermissions.VANISH)
  public void execute(@Sender Player sender, @Name("player") @Optional("self") Player player) {
    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    corePlayer.toggleVanish();

    this.taskDispatcher.runAsync(() -> corePlayer.save());

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(corePlayer.isVanished()) + " &avanish."));
  }

  @Command(value = {"vtest", "vanishtest"})
  @Permission(CorePermissions.DEV)
  public void vanishTest(@Sender Player sender, @Name("player") @Optional("self") Player player) {
    CorePlayer corePlayer = this.playerCache.getOrKick(player);

    sender.sendMessage(corePlayer.isVanished() + "");
  }
}

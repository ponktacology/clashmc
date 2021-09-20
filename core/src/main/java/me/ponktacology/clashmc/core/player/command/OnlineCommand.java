package me.ponktacology.clashmc.core.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class OnlineCommand {

  private final CorePlayerCache playerCache;

  @Command(value = "online", async = true)
  @Permission(CorePermissions.DEV)
  public void online(@Sender CommandSender sender, @Name("player") CorePlayer player) {
    sender.sendMessage("Online: " + this.playerCache.isOnlineNotInAuthOrLobby(player));
    sender.sendMessage("Server: " + this.playerCache.getCurrentServer(player).orElse("none"));
  }
}

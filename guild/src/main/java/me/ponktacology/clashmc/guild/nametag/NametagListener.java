package me.ponktacology.clashmc.guild.nametag;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.player.event.AsyncPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class NametagListener implements Listener {

  private final TaskDispatcher taskDispatcher;

  @EventHandler
  public void onPlayerJoin(AsyncPlayerJoinEvent event) {
    FrozenNametagHandler.initiatePlayer(event.getPlayer());
    FrozenNametagHandler.reloadPlayer(event.getPlayer());
    FrozenNametagHandler.reloadOthersFor(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    FrozenNametagHandler.getTeamMap().remove(event.getPlayer().getName());
  }
}

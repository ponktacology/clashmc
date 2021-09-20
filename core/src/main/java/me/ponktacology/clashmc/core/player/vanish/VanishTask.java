package me.ponktacology.clashmc.core.player.vanish;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.ActionBarUtil;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.entity.Player;



@RequiredArgsConstructor
public class VanishTask implements Runnable {


  private final CorePlayerCache playerCache;

  
  private static final String VANISH_MESSAGE = Text.colored("&aJesteś aktualnie ukryty.");

  @Override
  public void run() {
    for (CorePlayer corePlayer : this.playerCache.values()) {
      if (!corePlayer.isVanished()) continue;

      Player player = corePlayer.getPlayer();

      if (player == null) continue;

      ActionBarUtil.sendActionBarMessage(player, VANISH_MESSAGE);
    }
  }
}

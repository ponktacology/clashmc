package me.ponktacology.clashmc.drop.turbo.task;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.util.ActionBarUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.turbo.Turbo;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class TurboTask implements Runnable {


  private final DropPlayerCache playerCache;

  @Override
  public void run() {
    for (DropPlayer dropPlayer : this.playerCache.values()) {
      if (!dropPlayer.hasTurboDrop() && !dropPlayer.hasTurboExp()) continue;

      Player player = dropPlayer.getPlayer();

      if (player == null) continue;

      String turboDrop = dropPlayer.hasTurboDrop() ? "&e&lDROP" : "";
      String turboExp = dropPlayer.hasTurboExp() ? "&b&lEXP" : "";
      String message = "&d&lTURBO";

      if (!turboDrop.isEmpty()) {
        Turbo turbo = dropPlayer.getTurboDrop();
        message =
            message
                + turboDrop
                + "&f: "
                + TimeUtil.formatTimeMillis(
                    (turbo.getStartTimeStamp() + turbo.getDuration()) - System.currentTimeMillis())
                + " ";
      }

      if (!turboExp.isEmpty()) {
        Turbo turbo = dropPlayer.getTurboExp();
        message =
            message
                + turboExp
                + "&f: "
                + TimeUtil.formatTimeMillis(
                    (turbo.getStartTimeStamp() + turbo.getDuration()) - System.currentTimeMillis())
                + " ";
      }

      ActionBarUtil.sendActionBarMessage(player, Text.colored(message));
    }
  }
}

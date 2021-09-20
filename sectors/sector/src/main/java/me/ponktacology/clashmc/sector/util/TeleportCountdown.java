package me.ponktacology.clashmc.sector.util;

import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.ActionBarUtil;
import me.ponktacology.clashmc.core.util.Countdown;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportCountdown extends Countdown {

  private final CorePlayer corePlayer;
  private final Sector sector;
  private final Location location;
  private final PlayerTeleportUpdater teleportUpdater = SectorPlugin.INSTANCE.getTeleportUpdater();

  public TeleportCountdown(CorePlayer corePlayer, Sector sector, Location location) {
    super(6, true);
    this.corePlayer = corePlayer;
    this.sector = sector;
    this.location = location;

    Player player = corePlayer.getPlayer();

    if (player == null || this.teleportUpdater.isBeingTeleported(player)) {
      cancel();
      return;
    }

    this.teleportUpdater.setBeingTeleported(player, true);
  }

  @Override
  public void onTick(int time) {
    if (check()) {
      ActionBarUtil.sendActionBarMessage(
          corePlayer.getPlayer(), Text.colored("&eTeleportacja za: &f" + time));
    }
  }

  @Override
  public void onFinish() {
    Player player = corePlayer.getPlayer();
    ActionBarUtil.sendActionBarMessage(player, Text.colored("&aPomyślnie przeteleportowano."));
    this.teleportUpdater.setBeingTeleported(player, false);
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .run(
            () ->
                SectorPlugin.INSTANCE
                    .getTransferUpdater()
                    .update(player, this.sector, this.location));
  }

  private boolean check() {
    Player player = this.corePlayer.getPlayer();

    if (player == null || !this.teleportUpdater.isBeingTeleported(player)) {
      if (player != null) {
        this.teleportUpdater.setBeingTeleported(player, false);
        ActionBarUtil.sendActionBarMessage(player, Text.colored("&cAnulowano teleportację."));
      }

      cancel();

      return false;
    }

    return true;
  }
}

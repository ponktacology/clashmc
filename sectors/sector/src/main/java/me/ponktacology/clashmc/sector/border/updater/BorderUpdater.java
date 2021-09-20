package me.ponktacology.clashmc.sector.border.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.core.blazingpack.bar.BarColor;
import me.ponktacology.clashmc.core.blazingpack.bar.BarStyle;
import me.ponktacology.clashmc.core.blazingpack.bar.BossBar;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorConstants;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.border.cache.BorderCache;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;


import java.util.UUID;

@RequiredArgsConstructor
public class BorderUpdater implements Updater {

  private static final BossBar BORDER_BAR =
      new BossBar(
          UUID.fromString("d0e0e9ee-7ab5-4214-a679-3091db1bdea6"),
          BarStyle.SOLID,
          BarColor.YELLOW,
          TextComponent.fromLegacyText(""),
          0F);


  private final SectorCache sectorCache;

  private final BorderCache borderCache;

  private final Sector localSector;

  private final BarManager barManager;

  
  public void update( Player player) {
    double distance = RegionUtil.distanceToBorder(localSector, player.getLocation());

    if (distance <= 100) {
      this.borderCache.update(player);

      if (distance <= SectorConstants.MAX_DISTANCE_SHOW_BORDER) {
        float progress = RegionUtil.progress(distance);

        Sector sector =
            RegionUtil.getNearest(
                this.sectorCache, this.localSector, player.getLocation(), (int) distance + 1);

        String formatted =
            sector == null
                ? "mapy&f"
                : sector.isSpawn() ? "spawna&f" : "sektora &f" + sector.getName();
        this.barManager.update(
            player,
            BORDER_BAR,
            Text.colored(
                "&eOdległość do granicy " + formatted + " &7" + MathUtil.roundOff(distance, 2)),
            progress);
      } else {
        this.barManager.removeBar(player, BORDER_BAR);
      }
    } else {
      this.barManager.removeBar(player, BORDER_BAR);
    }
  }
}

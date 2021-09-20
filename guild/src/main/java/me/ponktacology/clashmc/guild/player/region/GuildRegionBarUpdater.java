package me.ponktacology.clashmc.guild.player.region;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.blazingpack.bar.BarColor;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.entity.Player;


import java.util.Optional;

@RequiredArgsConstructor
public class GuildRegionBarUpdater implements Updater {

  private final BarManager barManager;

  public void update( GuildPlayer guildPlayer) {
    Player player = guildPlayer.getPlayer();

    if (player == null) {
      return;
    }

    Optional<Guild> guildOptional = guildPlayer.getRegionCache().getLastGuild();

    if (!guildOptional.isPresent()) {
      this.barManager.removeBar(player, GuildConstants.TERRAIN_BAR);
      return;
    }

    Guild guild = guildOptional.get();

    float distance = (float) RegionUtil.distanceToLocation(player.getLocation(), guild.getCenter());
    float progress = 1.0F - (distance / guild.getRegionSize());

    String message;
    BarColor barColor;
    if (guild.hasMember(guildPlayer)) {
      message = "&aZnajdujesz się na terenie własnej gildii";

      if (guild.hasCreationProtection()) {
        message +=
            "\n&2Twoja gildia posiada ochronę przed &cTNT &2do "
                + TimeUtil.formatTimeMillisToDate(guild.creationProtectionExpireTime());
      }
      barColor = BarColor.GREEN;
    } else if (guildPlayer.getGuild().isPresent() && guildPlayer.getGuild().get().hasAlly(guild)) {
      message = "&9Znajdujesz się na terenie gildii &f" + guild.getTag();
      barColor = BarColor.BLUE;

      if (guild.hasCreationProtection()) {
        message +=
            "\n&1Ta gildia posiada ochronę przed &cTNT &1do "
                + TimeUtil.formatTimeMillisToDate(guild.creationProtectionExpireTime());
      }
    } else {
      message = "&cZnajdujesz się na terenie gildii &f" + guild.getTag();
      barColor = BarColor.RED;

      if (guild.hasCreationProtection()) {
        message +=
            "\n&4Ta gildia posiada ochronę przed &cTNT &4do "
                + TimeUtil.formatTimeMillisToDate(guild.creationProtectionExpireTime());
      }
    }

    this.barManager.update(
            player, GuildConstants.TERRAIN_BAR, Text.colored(message), barColor, progress);
  }

  @RequiredArgsConstructor
  public static class GuildRegionUpdateTask implements Runnable {


    private final GuildPlayerCache playerCache;

    private final GuildRegionBarUpdater guildRegionUpdater;

    @Override
    public void run() {
      for (GuildPlayer guildPlayer : this.playerCache.values()) {
        this.guildRegionUpdater.update(guildPlayer);
      }
    }
  }
}

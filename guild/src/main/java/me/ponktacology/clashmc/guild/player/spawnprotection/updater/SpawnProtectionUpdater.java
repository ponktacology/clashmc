package me.ponktacology.clashmc.guild.player.spawnprotection.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SpawnProtectionUpdater implements Updater {

  
  private final BarManager barManager;

  
  public void update( GuildPlayer guildPlayer) {
    Player player = guildPlayer.getPlayer();

    if (player == null) return;

    if (guildPlayer.hasSpawnProtection()) {
      if (guildPlayer.tickSpawnProtection()) {
        this.barManager.update(
            player,
            GuildConstants.PROTECTION_BAR,
            Text.colored(
                "&aOchrona startowa: &f"
                    + TimeUtil.formatTimeSeconds(guildPlayer.getSpawnProtectionTime())),
            guildPlayer.getSpawnProtectionProgress());
      }
    } else {
      this.barManager.removeBar(player, GuildConstants.PROTECTION_BAR);
    }
  }

  @RequiredArgsConstructor
  public static class SpawnProtectionUpdaterTask implements Runnable {

    
    private final GuildPlayerCache playerCache;
    
    private final SpawnProtectionUpdater spawnProtectionUpdater;

    @Override
    public void run() {
      for (GuildPlayer guildPlayer : this.playerCache.values()) {
        this.spawnProtectionUpdater.update(guildPlayer);
      }
    }
  }
}

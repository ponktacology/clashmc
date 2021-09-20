package me.ponktacology.clashmc.guild.player.combattag.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class CombatTagUpdater implements Updater {


  private final CombatTagSettingsCache combatTagSettingsCache;

  private final BarManager barManager;

  
  public void update( GuildPlayer guildPlayer) {
    Player player = guildPlayer.getPlayer();

    if (player == null) return;

    if (!this.combatTagSettingsCache.get().isEnabled()) {
      this.barManager.removeBar(player, GuildConstants.COMBAT_BAR);
      return;
    }

    if (guildPlayer.hasCombatTag()) {
      this.barManager.update(
          player,
          GuildConstants.COMBAT_BAR,
          "&cJesteś w walce jeszcze przez &f" + (int) guildPlayer.getCombatTagTime() + "&c sekund",
          guildPlayer.getCombatTagProgress());
    } else this.barManager.removeBar(player, GuildConstants.COMBAT_BAR);
  }

  @RequiredArgsConstructor
  public static class CombatTagUpdateTask implements Runnable {


    private final GuildPlayerCache playerCache;

    private final CombatTagUpdater combatTagUpdater;

    @Override
    public void run() {
      for (GuildPlayer guildPlayer : this.playerCache.values()) {
        this.combatTagUpdater.update(guildPlayer);
      }
    }
  }
}

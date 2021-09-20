package me.ponktacology.clashmc.queue.entry.updater;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.util.ActionBarUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.queue.entry.QueueEntry;
import me.ponktacology.clashmc.queue.entry.cache.QueueEntryCache;
import me.ponktacology.clashmc.queue.settings.QueueSettings;
import me.ponktacology.clashmc.queue.settings.cache.QueueSettingsCache;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.redirect.PlayerRedirectUpdater;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;

import java.util.Optional;

@RequiredArgsConstructor
public class QueueEntryUpdater implements Updater {

  private final SectorCache sectorCache;
  private final QueueEntryCache entryCache;
  private final PlayerRedirectUpdater redirectUpdater;
  private final CorePlayerCache playerCache;
  private final QueueSettingsCache settingsCache;

  public void update() {
    for (CorePlayer otherPlayer : this.playerCache.values()) {
      Player player = otherPlayer.getPlayer();

      if (player == null) continue;

      this.entryCache
          .getEntry(otherPlayer)
          .ifPresent(
              it -> {
                int position = this.entryCache.getPosition(it) + 1;

                player.sendTitle(
                    new Title(
                        "",
                        Text.colored("&a&lJesteś &7&l" + position + " &a&lw kolejce!"),
                        0,
                        24,
                        0));

                Rank rank = otherPlayer.getMainRank();

                ActionBarUtil.sendActionBarMessage(
                    player,
                    Text.colored(
                        "&7Nick: &f"
                            + otherPlayer.getName()
                            + " &8&l| &c&lClash&f&lMC.pl &8&l| &7Ranga: "
                            + rank.getColor()
                            + rank.getName()));
              });

      if (otherPlayer.isStaff()) this.redirectUpdater.update(otherPlayer.getUuid());
    }

    QueueSettings settings = this.settingsCache.get();

    if (!settings.isEnabled()
        || settings.getMaxPlayers() <= this.sectorCache.getNormalPlayersCount()) return;

    Optional<QueueEntry> entryOptional =
        settings.isAdvanced() ? this.entryCache.advancedNext() : this.entryCache.next();

    if (!entryOptional.isPresent()) return;

    QueueEntry entry = entryOptional.get();
    CorePlayer corePlayer = entry.getCorePlayer();

    if (corePlayer.isStaff()) return;

    this.redirectUpdater.update(entry.getCorePlayer().getUuid());
  }
}

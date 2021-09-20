package me.ponktacology.clashmc.queue.entry.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.queue.entry.QueueEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class QueueEntryCache extends KeyValueCache<UUID, QueueEntry> {

  private QueueEntry lastEntry;
  private int index;

  public void add(QueueEntry entry) {
    super.add(entry.getCorePlayer().getUuid(), entry);
  }

  public Optional<QueueEntry> next() {
    int size = this.size();

    if (size == 0) return Optional.empty();

    List<QueueEntry> entries = this.getValuesList();

    QueueEntry entry = entries.get(0);

    return Optional.of(entry);
  }

  public Optional<QueueEntry> advancedNext() {
    int size = this.size();

    if (size == 0) return Optional.empty();

    if (this.size() <= this.index) {
      this.index = 0;
      this.lastEntry = null;
      return this.advancedNext();
    }

    List<QueueEntry> entries = this.getValuesList();

    QueueEntry entry = entries.get(this.index);

    if (entry.equals(this.lastEntry)) {
      this.index++;
      return this.advancedNext();
    } else this.index = 0;

    this.lastEntry = entry;

    return Optional.of(entry);
  }

  public List<QueueEntry> getValuesList() {
    List<QueueEntry> entries = new ArrayList<>(this.values());

    entries.sort(
        (o1, o2) -> {
          Rank o1Rank = o1.getCorePlayer().getMainRank();
          Rank o2Rank = o2.getCorePlayer().getMainRank();

          int comparison = -(o1Rank.getPower() - o2Rank.getPower());

          return comparison == 0 ? (int) (o1.getTimeStamp() - o2.getTimeStamp()) : comparison;
        });

    return entries;
  }

  public Optional<QueueEntry> getEntry(CorePlayer corePlayer) {
    return this.get(corePlayer.getUuid());
  }

  public int getPosition(QueueEntry entry) {
    return this.getValuesList().indexOf(entry);
  }
}
